package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.models.Remboursement;
import com.groupeisi.minisystemebancaire.repositories.RemboursementRepository;
import com.groupeisi.minisystemebancaire.mappers.RemboursementMapper;
import com.groupeisi.minisystemebancaire.dto.RemboursementDTO;

import java.util.List;
import java.util.stream.Collectors;

public class RemboursementService {
    private final RemboursementRepository remboursementRepository = new RemboursementRepository();

    // ✅ Enregistrer un remboursement
    public void enregistrerRemboursement(RemboursementDTO remboursementDTO) {
        Remboursement remboursement = RemboursementMapper.toEntity(remboursementDTO);
        remboursementRepository.save(remboursement);
    }

    // ✅ Lire : Récupérer un remboursement par ID
    public RemboursementDTO getRemboursementById(Long id) {
        Remboursement remboursement = remboursementRepository.findById(id);
        if (remboursement == null) {
            throw new RuntimeException("Remboursement non trouvé !");
        }
        return RemboursementMapper.toDTO(remboursement);
    }

    // ✅ Lire : Récupérer tous les remboursements d'un crédit
    public List<RemboursementDTO> getRemboursementsByCredit(Long creditId) {
        return remboursementRepository.findByCreditId(creditId)
                .stream()
                .map(RemboursementMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Modifier un remboursement (Ex: Changer la date si erreur)
    public void updateRemboursement(Long id, String nouvelleDate) {
        Remboursement remboursement = remboursementRepository.findById(id);
        if (remboursement == null) {
            throw new RuntimeException("Remboursement non trouvé !");
        }
        remboursement.setDate(nouvelleDate);
        remboursementRepository.update(remboursement);
    }

    // ✅ Supprimer un remboursement (Si erreur)
    public void deleteRemboursement(Long id) {
        Remboursement remboursement = remboursementRepository.findById(id);
        if (remboursement == null) {
            throw new RuntimeException("Remboursement non trouvé !");
        }
        remboursementRepository.delete(remboursement);
    }
    public List<RemboursementDTO> getAllRemboursements() {
        return remboursementRepository.findAll()
                .stream()
                .map(RemboursementMapper::toDTO)
                .collect(Collectors.toList());
    }

}
