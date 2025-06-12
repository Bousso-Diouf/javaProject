package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.dto.CompteDTO;
import com.groupeisi.minisystemebancaire.models.Compte;
import com.groupeisi.minisystemebancaire.models.Transaction;
import com.groupeisi.minisystemebancaire.repositories.TransactionRepository;
import com.groupeisi.minisystemebancaire.mappers.TransactionMapper;
import com.groupeisi.minisystemebancaire.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CompteService compteService = new CompteService();
    // ✅ Créer une transaction
    public void enregistrerTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = TransactionMapper.toEntity(transactionDTO);
        transactionRepository.save(transaction);
    }

    // ✅ Lire : Récupérer une transaction par ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new RuntimeException("Transaction non trouvée !");
        }
        return TransactionMapper.toDTO(transaction);
    }

    // ✅ Lire : Récupérer toutes les transactions d'un compte
    public List<TransactionDTO> getTransactionsByCompte(Long compteId) {
        return transactionRepository.findByCompteSourceId(compteId)
                .stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Annuler une transaction suspecte
    public void annulerTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new RuntimeException("Transaction non trouvée !");
        }
        transaction.setStatut("Annulée");
        transactionRepository.update(transaction);
    }

    // ✅ Supprimer une transaction
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new RuntimeException("Transaction non trouvée !");
        }
        transactionRepository.delete(transaction);
    }

    // ✅ Récupérer toutes les transactions
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Récupérer les transactions suspectes
    public List<TransactionDTO> getTransactionsSuspectes() {
        return transactionRepository.findAll()
                .stream()
                .filter(this::isTransactionSuspecte)  // Filtrer les transactions suspectes
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Détermine si une transaction est suspecte
     * (Critères : montant élevé, transactions fréquentes, compte bloqué)
     */
    private boolean isTransactionSuspecte(Transaction transaction) {
        if (transaction == null) return false;

        Compte compteSource = transaction.getCompteSource();

        if (compteSource == null) {
            System.out.println("⚠️ Transaction avec compte source NULL détectée : " + transaction.getId());
            return false; // On ignore cette transaction pour éviter l'erreur
        }

        boolean montantEleve = transaction.getMontant() > 1_000_000; // Exemple : Montant > 1 000 000 FCFA
        boolean transactionRapide = transactionRepository.countRecentTransactions(transaction.getCompteSource().getId(), 5) >= 3;
        boolean compteBloque = transaction.getCompteSource().getStatut().equals("Bloqué");

        return montantEleve || transactionRapide || compteBloque;
    }
    // ✅ Mettre à jour une transaction existante
    public void updateTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(transactionDTO.getId());
        if (transaction == null) {
            throw new RuntimeException("Transaction non trouvée !");
        }

        // Mise à jour des données
        transaction.setStatut(transactionDTO.getStatut());
        transaction.setMontant(transactionDTO.getMontant());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());

        // Mise à jour en base de données
        transactionRepository.update(transaction);
    }

    public List<TransactionDTO> getTransactionsByClientId(Long clientId) {
        List<CompteDTO> comptes = compteService.getComptesByClientId(clientId);
        List<TransactionDTO> transactions = new ArrayList<>();

        for (CompteDTO compte : comptes) {
            List<TransactionDTO> transactionsCompte = transactionRepository.findByCompteSourceIdOrCompteDestId(compte.getId(), compte.getId())
                    .stream()
                    .map(TransactionMapper::toDTO)
                    .collect(Collectors.toList());

            transactions.addAll(transactionsCompte);
        }

        // Supprimer les doublons au cas où un compte aurait plusieurs transactions identiques
        return transactions.stream().distinct().collect(Collectors.toList());
    }


}
