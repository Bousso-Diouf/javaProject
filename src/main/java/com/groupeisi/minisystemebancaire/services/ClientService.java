package com.groupeisi.minisystemebancaire.services;

import com.groupeisi.minisystemebancaire.dto.ClientDTO;
import com.groupeisi.minisystemebancaire.mappers.ClientMapper;
import com.groupeisi.minisystemebancaire.models.Client;
import com.groupeisi.minisystemebancaire.repositories.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    private final ClientRepository clientRepository = new ClientRepository();

    // ✅ Inscription d'un nouveau client
    public void registerClient(ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByEmail(clientDTO.getEmail());
        if (existingClient != null) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        Client client = ClientMapper.toEntity(clientDTO);
        clientRepository.save(client);
    }

    // ✅ Connexion d'un client
    public ClientDTO login(String email, String password) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new RuntimeException("Aucun compte trouvé avec cet email !");
        }
        if (!client.getPassword().equals(password)) {
            throw new RuntimeException("Mot de passe incorrect !");
        }
        return ClientMapper.toDTO(client);
    }

    // ✅ Lire : Récupérer un client par ID
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new RuntimeException("Client non trouvé !");
        }
        return ClientMapper.toDTO(client);
    }

    // ✅ Lire : Récupérer tous les clients
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Mettre à jour un client
    public void updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new RuntimeException("Client non trouvé !");
        }
        client.setNom(clientDTO.getNom());
        client.setPrenom(clientDTO.getPrenom());
        client.setEmail(clientDTO.getEmail());
        client.setTelephone(clientDTO.getTelephone());
        client.setAdresse(clientDTO.getAdresse());
        client.setStatut(clientDTO.getStatut());
        clientRepository.update(client);
    }

    // ✅ Supprimer un client (seulement si pas de compte actif)
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new RuntimeException("Client non trouvé !");
        }
        if (!client.getComptes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un client avec des comptes actifs !");
        }
        clientRepository.delete(client);
    }
    public void suspendClient(Long clientId) {
        Client client = clientRepository.findById(clientId);
        if (client == null) {
            throw new RuntimeException("Client non trouvé !");
        }

        // Modifier le statut du client en "Suspendu"
        client.setStatut("Suspendu");

        clientRepository.update(client); // Assure-toi que `update()` existe dans le repository
    }
    // ✅ Réactivation d'un client suspendu
    public void reactivateClient(Long id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new RuntimeException("Client non trouvé !");
        }

        if (!client.getStatut().equals("Suspendu")) {
            throw new RuntimeException("Ce client n'est pas suspendu.");
        }

        client.setStatut("Actif");
        clientRepository.update(client);
    }


}
