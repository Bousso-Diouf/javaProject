package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.models.Admin;
import com.groupeisi.minisystemebancaire.mappers.AdminMapper;
import com.groupeisi.minisystemebancaire.dto.AdminDTO;
import com.groupeisi.minisystemebancaire.repositories.CarteBancaireRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AdminService {
    private final CarteBancaireRepository.AdminRepository adminRepository = new CarteBancaireRepository.AdminRepository();

    /**
     * ✅ Authentifie un administrateur par son username et mot de passe
     */
    public AdminDTO authentifierAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && admin.getPassword().equals(password)) { // ⚠️ Hachage recommandé !
            return new AdminDTO(admin.getId(), admin.getUsername(), admin.getRole());
        }
        return null;
    }
    // ✅ Authentification de l'admin
    public AdminDTO login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            throw new RuntimeException("Administrateur introuvable !");
        }
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect !");
        }
        return AdminMapper.toDTO(admin);
    }

    // ✅ Créer un nouvel admin
    public void createAdmin(AdminDTO adminDTO) {
        if (adminRepository.findByUsername(adminDTO.getUsername()) != null) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
        }
        Admin admin = AdminMapper.toEntity(adminDTO);
        adminRepository.save(admin);
    }

    // ✅ Lire : Récupérer un admin par ID
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrateur non trouvé !");
        }
        return AdminMapper.toDTO(admin);
    }

    // ✅ Lire : Récupérer tous les admins
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(AdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Mettre à jour un admin (changement de rôle, mot de passe)
    public void updateAdmin(Long id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrateur non trouvé !");
        }
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setRole(adminDTO.getRole());

        adminRepository.update(admin);
    }

    // ✅ Supprimer un admin (Sauf si c'est le dernier admin)
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrateur non trouvé !");
        }
        List<Admin> admins = adminRepository.findAll();
        if (admins.size() == 1) {
            throw new RuntimeException("Impossible de supprimer le dernier administrateur !");
        }
        adminRepository.delete(admin);
    }

}
