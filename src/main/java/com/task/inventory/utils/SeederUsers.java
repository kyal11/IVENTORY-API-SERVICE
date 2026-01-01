//package com.task.inventory.utils;
//
//import com.task.inventory.constant.UserRole;
//import com.task.inventory.entity.Users;
//import com.task.inventory.repository.UsersRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Component
//public class SeederUsers implements CommandLineRunner {
//
//    private final UsersRepository usersRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public SeederUsers(UsersRepository usersRepository) {
//        this.usersRepository = usersRepository;
//        this.passwordEncoder = new BCryptPasswordEncoder();
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        seedUsers();
//    }
//
//    private void seedUsers() {
//        createUserIfNotExists(
//                "a1111111-1111-1111-1111-111111111111",
//                "Admin Utama",
//                "admin@inventory.com",
//                "admin123",
//                UserRole.ADMIN
//        );
//
//        createUserIfNotExists(
//                "00000000-0000-0000-0000-000000000001",
//                "Auditor Internal",
//                "auditor@inventory.com",
//                "auditor123",
//                UserRole.AUDITOR
//        );
//
//        createUserIfNotExists(
//                "11111111-1111-1111-1111-111111111111",
//                "Rizky1",
//                "rizky1@inventory.com",
//                "rizky1",
//                UserRole.STAFF
//        );
//        createUserIfNotExists(
//                "22222222-2222-2222-2222-222222222222",
//                "Rizky2",
//                "rizky2@inventory.com",
//                "rizky2",
//                UserRole.STAFF
//        );
//        createUserIfNotExists(
//                "33333333-3333-3333-3333-333333333333",
//                "Rizky3",
//                "rizky3@inventory.com",
//                "rizky3",
//                UserRole.STAFF
//        );
//        createUserIfNotExists(
//                "44444444-4444-4444-4444-444444444444",
//                "Rizky4",
//                "rizky4@inventory.com",
//                "rizky4",
//                UserRole.STAFF
//        );
//
//        System.out.println("Users seeding completed!");
//    }
//
//    private void createUserIfNotExists(String uuid, String name, String email, String rawPassword, UserRole role) {
//        Optional<Users> existingUser = usersRepository.findById(UUID.fromString(uuid));
//        if (existingUser.isPresent()) {
//            System.out.println("User already exists: " + name);
//            return; // skip insert
//        }
//
//        Users user = new Users();
//        user.setId(UUID.fromString(uuid));
//        user.setName(name);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(role);
//
//        usersRepository.save(user);
//        System.out.println("Created user: " + name);
//    }
//}
