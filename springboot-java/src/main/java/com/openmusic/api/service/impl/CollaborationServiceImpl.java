package com.openmusic.api.service.impl;

import com.openmusic.api.entities.database.Collaborations;
import com.openmusic.api.entities.database.Playlists;
import com.openmusic.api.entities.database.Users;
import com.openmusic.api.exception.ApplicationException;
import com.openmusic.api.exception.ClientException;
import com.openmusic.api.exception.EntityNotFoundException;
import com.openmusic.api.repository.jpa.CollaborationsRepository;
import com.openmusic.api.service.PlaylistService;
import com.openmusic.api.service.UserService;
import com.openmusic.api.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Ahmad Irfaan Hibatullah
 * @version $Id: CollaborationServiceImpl.java, v 0.1 2021‐10‐20 08.12 Ahmad Irfaan Hibatullah Exp $$
 */

@Service
public class CollaborationServiceImpl implements CollaborationService {

    protected final CollaborationsRepository collaborationsRepository;

    protected final PlaylistService playlistService;

    protected final UserService userService;

    @Autowired
    public CollaborationServiceImpl(CollaborationsRepository collaborationsRepository,
                                    PlaylistService playlistService,
                                    UserService userService) {
        this.collaborationsRepository = collaborationsRepository;
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @Override
    public Collaborations addCollaboration(String playlistId, String userId) {
        Collaborations collaborations = new Collaborations();
        if(verifyPlaylistAndUserExist(playlistId, userId)) {
            Playlists playlists = playlistService.findPlaylistById(playlistId);
            Users users = userService.findByUserId(userId);
            collaborations.setPlaylists(playlists);
            collaborations.setUsers(users);
            collaborations = collaborationsRepository.save(collaborations);
            return collaborations;

        } else {
            throw new ApplicationException("Kolaborasi gagal ditambahkan");
        }
    }

    @Override
    public Collaborations deleteCollaboration(String playlistId, String userId) {
        if (verifyPlaylistAndUserExist(playlistId, userId)) {
            Collaborations collaborations = collaborationsRepository.findByPlaylistsAndUsers(
                    playlistId, userId
            );
            collaborations.setDeletedDate(LocalDateTime.now());
            return collaborations;
        } else {
            throw new ClientException("Kolaborasi gagal dihapus");
        }
    }

    @Override
    public Collaborations verifyCollaborator(String playlistId, String userId) {
        Collaborations collaborations = new Collaborations();
        if(verifyPlaylistAndUserExist(playlistId, userId)) {
            collaborations = collaborationsRepository.findByPlaylistsAndUsers(
                    playlistId, userId
            );
            if(collaborations == null || collaborations.getDeletedDate() != null) {
                throw new ClientException("Kolaborasi gagal diverifikasi");
            }
        }
        return collaborations;
    }

    private Boolean verifyPlaylistAndUserExist(String playlistId, String userId) {
        Playlists playlists = playlistService.findPlaylistById(playlistId);
        if(playlists == null || playlists.getDeletedDate() != null) {
            throw new EntityNotFoundException("Playlist Not Found");
        }
        Users users = userService.findByUserId(userId);
        if(users == null || users.getDeletedDate() != null) {
            throw new EntityNotFoundException("Users Not Found");
        }
        return true;
    }

    @Override
    public Boolean verifyPlaylistAccess(String playlistId, String owner) {
        try {
            playlistService.verifyPlaylistOwner(playlistId, owner);
        } catch (Exception e) {
            if(e instanceof EntityNotFoundException) {
                throw e;
            }
            try {
                verifyCollaborator(playlistId, owner);
            } catch (Exception ex){
                throw ex;
            }
        }
        return true;
    }
}