package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.models.Credit;
import com.groupeisi.minisystemebancaire.repositories.CreditRepository;
import com.groupeisi.minisystemebancaire.mappers.CreditMapper;
import com.groupeisi.minisystemebancaire.dto.CreditDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CreditService {
    private final CreditRepository creditRepository = new CreditRepository();

    // ✅ Demander un crédit
    public void demanderCredit(CreditDTO creditDTO) {
        Credit credit = CreditMapper.toEntity(creditDTO);
        credit.setStatut("En attente"); // Par défaut, un crédit est "En attente"
        creditRepository.save(credit);
    }

    // ✅ Lire : Récupérer un crédit par ID
    public CreditDTO getCreditById(Long id) {
        Credit credit = creditRepository.findById(id);
        if (credit == null) {
            throw new RuntimeException("Crédit non trouvé !");
        }
        return CreditMapper.toDTO(credit);
    }

    // ✅ Lire : Récupérer tous les crédits d'un client
    public List<CreditDTO> getCreditsByClient(Long clientId) {
        return creditRepository.findByClientId(clientId)
                .stream()
                .map(CreditMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lire : Récupérer les crédits par statut (En attente, Accepté, Refusé)
    public List<CreditDTO> getCreditsByStatut(String statut) {
        return creditRepository.findByStatut(statut)
                .stream()
                .map(CreditMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Accepter un crédit
    public void accepterCredit(Long id) {
        Credit credit = creditRepository.findById(id);
        if (credit == null) {
            throw new RuntimeException("Crédit non trouvé !");
        }

        credit.setStatut("Accepté");
        creditRepository.update(credit);
    }

    // ✅ Refuser un crédit
    public void refuserCredit(Long id) {
        Credit credit = creditRepository.findById(id);
        if (credit == null) {
            throw new RuntimeException("Crédit non trouvé !");
        }

        credit.setStatut("Refusé");
        creditRepository.update(credit);
    }

    // ✅ Modifier un crédit (Mise à jour du statut ou autres informations)
    public void updateCredit(Long id, CreditDTO creditDTO) {
        Credit credit = creditRepository.findById(id);
        if (credit == null) {
            throw new RuntimeException("Crédit non trouvé !");
        }

        credit.setMontant(creditDTO.getMontant());
        credit.setDureeMois(creditDTO.getDureeMois());
        credit.setTauxInteret(creditDTO.getTauxInteret());
        credit.setStatut(creditDTO.getStatut());
        creditRepository.update(credit);
    }

    // ✅ Supprimer un crédit (Seulement si non approuvé)
    public void deleteCredit(Long id) {
        Credit credit = creditRepository.findById(id);
        if (credit == null) {
            throw new RuntimeException("Crédit non trouvé !");
        }

        if (!credit.getStatut().equals("En attente")) {
            throw new RuntimeException("Impossible de supprimer un crédit déjà validé !");
        }

        creditRepository.delete(credit);
    }

    // ✅ Lire : Récupérer tous les crédits
    public List<CreditDTO> getAllCredits() {
        return creditRepository.findAll()
                .stream()
                .map(CreditMapper::toDTO)
                .collect(Collectors.toList());
    }
}
