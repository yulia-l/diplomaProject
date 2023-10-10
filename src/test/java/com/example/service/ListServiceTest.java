package com.example.service;

import com.example.dto.FileDTO;
import com.example.entity.FileEntity;
import com.example.entity.User;
import com.example.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListServiceTest {
    @Mock
    private FileRepository fileRepository;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private ListService listService;

    @Test
    public void testGetFilesSuccess() {
        String authToken = "testGetFilesSuccessToken";
        Integer limit = 1;
        User user = new User();
        user.setId(1L);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename("file.txt");
        fileEntity.setSize(1024L);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(fileEntity));

        List<FileDTO> result = listService.getFiles(authToken, limit);

        verify(tokenService).getUserByAuthToken(authToken);
        verify(fileRepository).findAllByUserId(user.getId());

        assertEquals(1, result.size());
        assertEquals("file.txt", result.get(0).getFilename());
        assertEquals(1024L, (long) result.get(0).getSize());
    }

    @Test(expected = ResponseStatusException.class)
    public void testGetFilesUnauthorized() {
        String authToken = "testGetFilesUnauthorizedToken";
        Integer limit = 1;

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(null);

        listService.getFiles(authToken, limit);
    }
}
