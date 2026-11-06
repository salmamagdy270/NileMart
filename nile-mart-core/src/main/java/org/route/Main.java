package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    void main(){

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class);
             var scanner = new Scanner(System.in)) {
            ProductService productService = context.getBean(ProductService.class);
            CheckoutService checkoutService = context.getBean(CheckoutService.class);
            Map<Integer, ShoppingCart> carts = new LinkedHashMap<>();
            int nextCartNumber = 1;
            boolean running = true;

            while (running) {
                System.out.println("\n========== NILEMART ==========");
                System.out.println("1. Show products");
                System.out.println("2. Create a cart");
                System.out.println("3. Add a product to a cart");
                System.out.println("4. Show a cart");
                System.out.println("5. Check out a cart");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" -> productService.findAll().forEach(product ->
                                System.out.printf("%s | %s | %s EGP | Stock: %d | %s%n",
                                        product.getSku(), product.getName(), product.getPrice(),
                                        product.getStockQuantity(), product.getCategory()));
                        case "2" -> {
                            System.out.print("Customer full name: ");
                            String name = scanner.nextLine();
                            System.out.print("Phone: ");
                            String phone = scanner.nextLine();
                            System.out.print("Email (optional, press Enter to skip): ");
                            String email = scanner.nextLine();
                            Customer customer = new Customer(name, phone, email.isBlank() ? null : email);
                            ShoppingCart cart = context.getBean(ShoppingCart.class);
                            cart.initialize(nextCartNumber, customer);
                            carts.put(nextCartNumber, cart);
                            System.out.println("Created cart #" + nextCartNumber);
                            nextCartNumber++;
                        }
                        case "3" -> {
                            System.out.print("Cart number: ");
                            int cartNumber = Integer.parseInt(scanner.nextLine().trim());
                            ShoppingCart cart = requireCart(carts, cartNumber);
                            System.out.print("Product SKU: ");
                            String sku = scanner.nextLine().trim();
                            System.out.print("Quantity: ");
                            int quantity = Integer.parseInt(scanner.nextLine().trim());
                            cart.addItem(sku, quantity);
                            System.out.println("Product added.");
                        }
                        case "4" -> {
                            System.out.print("Cart number: ");
                            int cartNumber = Integer.parseInt(scanner.nextLine().trim());
                            ShoppingCart cart = requireCart(carts, cartNumber);
                            System.out.println("Cart #" + cartNumber + " - " + cart.getCustomer().fullName());
                            if (cart.isEmpty()) System.out.println("Cart is empty.");
                            for (Map.Entry<String, Integer> line : cart.getLines().entrySet()) {
                                Product product = productService.findBySku(line.getKey());
                                System.out.printf("%s x %d = %s EGP%n", product.getName(), line.getValue(),
                                        productService.getPrice(line.getKey(), line.getValue()));
                            }
                            System.out.println("Subtotal: " + cart.subtotal() + " EGP");
                        }
                        case "5" -> {
                            System.out.print("Cart number: ");
                            int cartNumber = Integer.parseInt(scanner.nextLine().trim());
                            ShoppingCart cart = requireCart(carts, cartNumber);
                            System.out.print("Governorate: ");
                            Governorate governorate = Governorate.fromInput(scanner.nextLine());
                            Order order = checkoutService.placeOrder(cart, governorate);
                            System.out.println("Checkout successful!");
                            System.out.println("Order number: " + order.orderNumber());
                            System.out.println("Subtotal: " + order.subtotal());
                            System.out.println("VAT: " + order.vat());
                            System.out.println("Shipping: " + order.shippingFee());
                            System.out.println("Total: " + order.total() + " EGP");
                        }
                        case "0" -> running = false;
                        default -> System.out.println("Invalid option. Try again.");
                    }
                } catch (StoreException exception) {
                    System.out.println("Error: " + exception.getMessage());
                } catch (NumberFormatException exception) {
                    System.out.println("Please enter a valid number.");
                }
            }
        }
    }

    private static ShoppingCart requireCart(Map<Integer, ShoppingCart> carts, int cartNumber) {
        ShoppingCart cart = carts.get(cartNumber);
        if (cart == null) throw new StoreException("Cart #" + cartNumber + " does not exist.");
        return cart;

    }
}
