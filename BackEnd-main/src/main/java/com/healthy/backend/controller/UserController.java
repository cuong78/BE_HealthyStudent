package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.ExportService;
import com.healthy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Controller", description = "Users related management APIs")
public class UserController {

    private final UserService userService;
    private final ExportService exportService;
    private final TokenService tokenService;

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users."
    )
    @GetMapping("/all")
    public ResponseEntity<List<UsersResponse>> getAllUsers(HttpServletRequest request) {
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("You can not get all users");
        }
        List<UsersResponse> users = userService.getAllUsers();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user with the specified ID."
    )
    @GetMapping("")
    public ResponseEntity<UsersResponse> getUserById(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);
        if (tokenService.validateUID(request, userId) && !tokenService.isManager(request)) {
            throw new OperationFailedException("You can not get other users details");
        }
        UsersResponse user = userService.getUserById(userId);
        return user == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user details with the specified ID."
    )
    @GetMapping("/details")
    public ResponseEntity<UsersResponse> getUserDetailsById(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);
        if (tokenService.validateUID(request, userId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You can not get other users details");
        }
        UsersResponse user = userService.getUserDetailsById(userId);
        return user == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update user details",
            description = "Updates a user's details."
    )
    @PutMapping("/update")
    public ResponseEntity<UsersResponse> updateUser(
            @RequestParam(required = false) String userId,
            @RequestBody Users updatedUser,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);
        if (tokenService.validateUID(request, userId) && !tokenService.isManager(request)) {
            throw new OperationFailedException("You can not update other user details");
        }
        UsersResponse updatedUserResponse = userService.updateUser(userId, updatedUser);
        return updatedUserResponse == null
                ? ResponseEntity.noContent().build() // 204 if no changes detected
                : ResponseEntity.ok(updatedUserResponse); // 200 if update successful
    }

    @Operation(
            summary = "Deactivate user account",
            description = "Deactivates a user's account."
    )
    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(
            @RequestParam String userId,
            HttpServletRequest request) {
        // Get the user ID from the request
        userId = tokenService.validateRequestUserID(request, userId);
        // Check if the user is a manager
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to deactivate this user");
        }
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }

    @Operation(
            summary = "Reactivate user account",
            description = "Reactivates a user's account."
    )
    @PostMapping("/reactivate")
    public ResponseEntity<?> reactivateUser(
            @RequestParam String userId, HttpServletRequest request) {
        // Get the user ID from the request
        userId = tokenService.validateRequestUserID(request, userId);
        // Check if the user is a manager
        if (!tokenService.isManager(request) && tokenService.validateUID(request, userId)) {
            throw new OperationFailedException("You don't have permission to reactivate this user");
        }
        return ResponseEntity.ok(userService.reactivateUser(userId));
    }

    @Operation(
            hidden = true,
            deprecated = true,
            summary = "Update user role",
            description = "Updates a user's role."
    )
    @PutMapping("/role")
    public ResponseEntity<?> updateUserRole(
            @RequestParam String userId,
            @RequestParam String role,
            HttpServletRequest request) {
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("You can not update other user role");
        }
        return ResponseEntity.ok(userService.updateUserRole(userId, role));
    }

    @Operation(
            summary = "Export user data",
            description = "Exports user data in a specified format (CSV, JSON, or PDF)."
    )
    @GetMapping("/export")
    public ResponseEntity<?> exportUserData(
            @RequestParam String userId,
            @RequestParam String format) {
        byte[] exportedData;
        return switch (format.toLowerCase()) {
            case "csv" -> {
                exportedData = exportService.exportUserData(userId, "csv");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".csv")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(exportedData);
            }
            case "json" -> {
                exportedData = exportService.exportUserData(userId, "json");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(exportedData);
            }
            case "pdf" -> {
                exportedData = exportService.exportUserData(userId, "pdf");
                yield ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_" + userId + ".pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(exportedData);
            }
            default -> ResponseEntity.badRequest().body("Invalid format. Supported formats: csv, json, pdf.");
        };
    }

    @Operation(
            summary = "Submit feedback for user",
            description = "Submits feedback for a specific user."
    )
    @PostMapping("/feedback")
    public String submitFeedback(@RequestParam String userId, @RequestBody String feedback) {
        return "Feedback submitted by user " + userId;
    }

    @Operation(
            summary = "Search users by name",
            description = "Searches for users with a specific name."
    )
    @GetMapping("/search")
    public ResponseEntity<List<UsersResponse>> searchUsers(
            @RequestParam String name, HttpServletRequest request) {
        List<UsersResponse> list = userService.searchUsers(name);
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("You can not search for users");
        }
        return list.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Delete user account",
            description = "Deletes a user's account."
    )
    @DeleteMapping("/delete")
    public ResponseEntity<UsersResponse> deleteUser(
            @RequestParam String userId,
            HttpServletRequest request) {
        if (!tokenService.isManager(request)) {
            throw new OperationFailedException("You can not delete users");
        }
        if (userService.deleteUser(userId)) {
            return ResponseEntity.ok(userService.getUserById(userId));
        }
        throw new OperationFailedException("Failed to delete user account");
    }

    @Operation(
            summary = "Get all events",
            description = "Returns a list of all events for a specific user."
    )
    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);
        if (tokenService.validateUID(request, userId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You can not get other users events");
        }
        EventResponse events = userService.getAllEvents(userId);
        return events == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Get user's appointments",
            description = "Returns all appointments for a specific user (student or psychologist)."
    )
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getUserAppointments(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);
        if (tokenService.validateUID(request, userId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You can not get other users appointments");
        }
        List<AppointmentResponse> appointments = userService.getUserAppointment(userId);
        return appointments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(appointments);
    }
}