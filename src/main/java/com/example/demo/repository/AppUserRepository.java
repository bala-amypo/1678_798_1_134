package com.example.demo.repository;

import com.example.demo.model.AppUser;
import com.example.demo.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
    // Find by email
    Optional<AppUser> findByEmail(String email);
    
    // Check if email exists
    Boolean existsByEmail(String email);
    
    // Find by role
    List<AppUser> findByRole(UserRole role);
    
    // Find active users by role
    List<AppUser> findByRoleAndActiveTrue(UserRole role);
    
    // Find all active users
    List<AppUser> findByActiveTrue();
    
    // Find users by role and specialization
    List<AppUser> findByRoleAndSpecializationAndActiveTrue(UserRole role, String specialization);
    
    // Find users who haven't logged in recently
    @Query("SELECT u FROM AppUser u WHERE u.lastLoginAt < :cutoffDate OR u.lastLoginAt IS NULL")
    List<AppUser> findUsersWithInactiveLogin(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Count users by role
    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.role = :role AND u.active = true")
    Long countActiveUsersByRole(@Param("role") UserRole role);
    
    // Find users by partial name match
    @Query("SELECT u FROM AppUser u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) AND u.active = true")
    List<AppUser> searchByFullName(@Param("name") String name);
    
    // Find clinicians with specific license number
    List<AppUser> findByLicenseNumberAndRoleAndActiveTrue(String licenseNumber, UserRole role);
    
    // Find users created after specific date
    List<AppUser> findByCreatedAtAfter(LocalDateTime date);
    
    // Find users updated after specific date
    List<AppUser> findByUpdatedAtAfter(LocalDateTime date);
    
    // Find by phone number
    Optional<AppUser> findByPhoneNumber(String phoneNumber);
    
    // Check if phone number exists
    Boolean existsByPhoneNumber(String phoneNumber);
    
    // Find users with email domain
    @Query("SELECT u FROM AppUser u WHERE u.email LIKE %:domain% AND u.active = true")
    List<AppUser> findByEmailDomain(@Param("domain") String domain);
}