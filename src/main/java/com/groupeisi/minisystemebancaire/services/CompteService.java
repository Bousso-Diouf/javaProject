package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.dto.CompteDTO;
import com.groupeisi.minisystemebancaire.mappers.CompteMapper;
import com.groupeisi.minisystemebancaire.models.Compte;
import com.groupeisi.minisystemebancaire.repositories.CompteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CompteService {
    private final CompteRepository compteRepository = new CompteRepository();

    // ✅ Créer un compte
    public void createCompte(CompteDTO compteDTO) {
        Compte compte = CompteMapper.toEntity(compteDTO);
        compteRepository.save(compte);
    }

    // ✅ Lire : Récupérer un compte par ID
    public CompteDTO getCompteById(Long id) {
        Compte compte = compteRepository.findById(id);
        if (compte == null) {
            throw new RuntimeException("Compte non trouvé !");
        }
        return CompteMapper.toDTO(compte);
    }

    // ✅ Lire : Récupérer un compte par son **numéro**
    public CompteDTO getCompteByNumero(String numero) {
        Compte compte = compteRepository.findByNumero(numero);
        if (compte == null) {
            throw new RuntimeException("Compte avec le numéro " + numero + " non trouvé !");
        }
        return CompteMapper.toDTO(compte);
    }

    // ✅ Lire : Récupérer les comptes d'un client
    public List<CompteDTO> getComptesByClientId(Long clientId) {
        return compteRepository.findByClientId(clientId)
                .stream()
                .map(CompteMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Mettre à jour un compte (changement de type ou de statut)
    public void updateCompte(Long id, CompteDTO compteDTO) {
        Compte compte = compteRepository.findById(id);
        if (compte == null) {
            throw new RuntimeException("Compte non trouvé !");
        }
        compte.setType(compteDTO.getType());
        compte.setSolde(compteDTO.getSolde());
        compte.setStatut(compteDTO.getStatut()); // Ajout de la mise à jour du statut
        compteRepository.update(compte);
    }

    // ✅ Supprimer un compte (seulement si solde = 0)
    public void deleteCompte(Long id) {
        Compte compte = compteRepository.findById(id);
        if (compte == null) {
            throw new RuntimeException("Compte non trouvé !");
        }
        if (compte.getSolde() > 0) {
            throw new RuntimeException("Impossible de supprimer un compte avec un solde positif !");
        }
        compteRepository.delete(compte);
    }
    public List<CompteDTO> getAllComptes() {
        return compteRepository.findAll()
                .stream()
                .map(CompteMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void appliquerFrais(Long compteId, double montantFrais) {
        // Vérifier si le compte existe
        Compte compte = compteRepository.findById(compteId);
        if (compte == null) {
            throw new RuntimeException("Compte non trouvé !");
        }

        // Vérifier si le solde est suffisant pour appliquer les frais
        if (compte.getSolde() < montantFrais) {
            throw new RuntimeException("Fonds insuffisants pour appliquer les frais !");
        }

        // Déduire les frais du solde
        compte.setSolde(compte.getSolde() - montantFrais);

        // Mettre à jour le compte
        compteRepository.update(compte);
    }
    public void fermerCompte(Long compteId) {
        // Vérifier si le compte existe
        Compte compte = compteRepository.findById(compteId);
        if (compte == null) {
            throw new RuntimeException("Compte non trouvé !");
        }

        // Vérifier si le compte a un solde de 0 avant de le fermer
        if (compte.getSolde() > 0) {
            throw new RuntimeException("Impossible de fermer un compte avec un solde positif !");
        }

        // Changer le statut du compte en "Fermé"
        compte.setStatut("Fermé");

        // Mettre à jour le compte
        compteRepository.update(compte);
    }


}
