package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.models.CarteBancaire;
import com.groupeisi.minisystemebancaire.repositories.CarteBancaireRepository;
import com.groupeisi.minisystemebancaire.mappers.CarteBancaireMapper;
import com.groupeisi.minisystemebancaire.dto.CarteBancaireDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CarteBancaireService {
    private final CarteBancaireRepository carteBancaireRepository = new CarteBancaireRepository();

    // ✅ Récupérer une carte par son numéro
    public CarteBancaireDTO getCarteByNumero(String numeroCarte) {
        CarteBancaire carte = carteBancaireRepository.findByNumero(numeroCarte);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        return CarteBancaireMapper.toDTO(carte);
    }
    /**
     * ✅ Créer une carte bancaire pour un client
     */
    public void demanderCarte(CarteBancaireDTO carteDTO) {
        CarteBancaire carte = CarteBancaireMapper.toEntity(carteDTO);
        carte.setStatut("Active"); // Par défaut, une carte est active
        carteBancaireRepository.save(carte);
    }

    /**
     * ✅ Lire : Récupérer une carte par ID
     */
    public CarteBancaireDTO getCarteById(Long id) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        return CarteBancaireMapper.toDTO(carte);
    }

    /**
     * ✅ Lire : Récupérer toutes les cartes d'un compte
     */
    public List<CarteBancaireDTO> getCartesByCompte(Long compteId) {
        return carteBancaireRepository.findByCompteId(compteId)
                .stream()
                .map(CarteBancaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Lire : Récupérer toutes les cartes bancaires
     */
    public List<CarteBancaireDTO> getAllCartes() {
        return carteBancaireRepository.findAll()
                .stream()
                .map(CarteBancaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Modifier une carte (Ex: Mise à jour du statut)
     */
    public void updateCarte(CarteBancaireDTO carteDTO) {
        CarteBancaire carte = carteBancaireRepository.findById(carteDTO.getId());
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        carte.setStatut(carteDTO.getStatut());
        carteBancaireRepository.update(carte);
    }
    // ✅ Bloquer ou Débloquer une carte
    public void updateCarteStatut(Long id, String statut) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        carte.setStatut(statut);
        carteBancaireRepository.update(carte);
    }

    /**
     * ✅ Bloquer une carte bancaire
     */
    public void bloquerCarte(Long id) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        carte.setStatut("Bloquée");
        carteBancaireRepository.update(carte);
    }

    /**
     * ✅ Débloquer une carte bancaire
     */
    public void debloquerCarte(Long id) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        carte.setStatut("Active");
        carteBancaireRepository.update(carte);
    }

    /**
     * ✅ Vérifier si une carte est valide (ex: non expirée)
     */
    public boolean isCarteValide(Long id) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            return false;
        }
        return !carte.getStatut().equals("Expirée");
    }

    /**
     * ✅ Supprimer une carte bancaire si elle est expirée
     */
    public void deleteCarte(Long id) {
        CarteBancaire carte = carteBancaireRepository.findById(id);
        if (carte == null) {
            throw new RuntimeException("Carte non trouvée !");
        }
        if (!carte.getStatut().equals("Expirée")) {
            throw new RuntimeException("Impossible de supprimer une carte active !");
        }
        carteBancaireRepository.delete(carte);
    }
    // ✅ Ajout de la méthode pour récupérer toutes les cartes d’un client
    public List<CarteBancaireDTO> getCartesByClient(Long clientId) {
        return carteBancaireRepository.findByClientId(clientId)
                .stream()
                .map(CarteBancaireMapper::toDTO)
                .collect(Collectors.toList());
    }
}
