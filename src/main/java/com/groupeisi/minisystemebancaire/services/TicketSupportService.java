package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.models.TicketSupport;
import com.groupeisi.minisystemebancaire.repositories.TicketSupportRepository;
import com.groupeisi.minisystemebancaire.mappers.TicketSupportMapper;
import com.groupeisi.minisystemebancaire.dto.TicketSupportDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TicketSupportService {
    private final TicketSupportRepository ticketSupportRepository = new TicketSupportRepository();

    // ✅ Soumettre une réclamation (Créer un ticket)
    public void soumettreTicket(TicketSupportDTO ticketDTO) {
        TicketSupport ticket = TicketSupportMapper.toEntity(ticketDTO);
        ticket.setStatut("Ouvert"); // Par défaut, un ticket est "Ouvert"
        ticketSupportRepository.save(ticket);
    }

    // ✅ Lire : Récupérer un ticket par ID
    public TicketSupportDTO getTicketById(Long id) {
        TicketSupport ticket = ticketSupportRepository.findById(id);
        if (ticket == null) {
            throw new RuntimeException("Ticket non trouvé !");
        }
        return TicketSupportMapper.toDTO(ticket);
    }

    // ✅ Lire : Récupérer tous les tickets d'un client
    public List<TicketSupportDTO> getTicketsByClient(Long clientId) {
        return ticketSupportRepository.findByClientId(clientId)
                .stream()
                .map(TicketSupportMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lire : Récupérer tous les tickets
    public List<TicketSupportDTO> getAllTickets() {
        return ticketSupportRepository.findAll()
                .stream()
                .map(TicketSupportMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Rechercher un ticket par ID ou Sujet
    public List<TicketSupportDTO> rechercherTicket(String recherche) {
        return ticketSupportRepository.findBySujetOrId(recherche)
                .stream()
                .map(TicketSupportMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Répondre à un ticket
    public void repondreTicket(Long ticketId, String reponse) {
        TicketSupport ticket = ticketSupportRepository.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket non trouvé !");
        }
        ticket.setReponse(reponse);
        ticket.setStatut("Répondu");
        ticketSupportRepository.update(ticket);
    }

    // ✅ Marquer un ticket comme résolu
    public void marquerTicketResolu(Long ticketId) {
        TicketSupport ticket = ticketSupportRepository.findById(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket non trouvé !");
        }
        ticket.setStatut("Résolu");
        ticketSupportRepository.update(ticket);
    }

    // ✅ Générer un rapport (Simulé)
    public void genererRapport(String type, String periode) {
        System.out.println("📊 Rapport généré : " + type + " pour " + periode);
    }

    // ✅ Supprimer un ticket (Si résolu)
    public void deleteTicket(Long id) {
        TicketSupport ticket = ticketSupportRepository.findById(id);
        if (ticket == null) {
            throw new RuntimeException("Ticket non trouvé !");
        }
        if (!ticket.getStatut().equals("Résolu")) {
            throw new RuntimeException("Impossible de supprimer un ticket non résolu !");
        }
        ticketSupportRepository.delete(ticket);
    }
    // ✅ Mettre à jour un ticket
    public void updateTicket(TicketSupportDTO ticketDTO) {
        TicketSupport ticket = ticketSupportRepository.findById(ticketDTO.getId());
        if (ticket == null) {
            throw new RuntimeException("Ticket non trouvé !");
        }
        ticket.setStatut(ticketDTO.getStatut());
        ticket.setReponse(ticketDTO.getReponse());} // Mise à jour de la réponse

    }
