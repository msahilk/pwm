package pwm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static pwm.DBConnect.copyPassword;
import static pwm.DBConnect.getPasswords;

@RestController
@RequestMapping("/api")
public class PasswordController {

    @GetMapping("/passwords")
    public ResponseEntity<List<Password>> getAllPasswords() {
        // Example: Create a mock list of Passwords
        List<Password> passwords = getPasswords();
        // Return the list as a JSON response
        return ResponseEntity.ok(passwords);
    }

    @GetMapping("/password")
    public ResponseEntity<String> getPassword(
            @RequestParam("website") String website,
            @RequestParam("username") String username) {

        // Use the service to retrieve the password
        String password = copyPassword(website, username);

        // Check if the password is found
        // Check if the password is found and properly decoded
            return ResponseEntity.ok(password);  // Return 200 OK with the password as a stri// Return 400 Bad Request with the error message
        }

    @DeleteMapping("/password")
    public ResponseEntity<String> deletePassword(
            @RequestParam("website") String website,
            @RequestParam("username") String username) {

        boolean isDeleted = DBConnect.deletePassword(website, username); // Implement this method to delete the password from your database
        if (isDeleted) {
            return ResponseEntity.ok("Password deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        }
    }

    @PostMapping("/password")
    public ResponseEntity<String> addPassword(
            @RequestParam("website") String website,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Password pw = new Password(website,username,password);

        String resp = DBConnect.addPassword(pw); // Implement this method to delete the password from your databas
        return ResponseEntity.ok(resp);
    }
}


