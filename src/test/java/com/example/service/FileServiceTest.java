package com.example.service;

import com.example.entity.FileEntity;
import com.example.entity.User;
import com.example.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private FileRepository fileRepository;
    @InjectMocks
    private FileService fileService;

    @Test
    public void testUploadFileSuccess() {
        String authToken = "token";
        String filename = "file.txt";
        MultipartFile file = new MockMultipartFile("file", filename, "text/plain", "test data".getBytes());
        User user = new User();
        user.setId(1L);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);

        fileService.uploadFile(authToken, filename, file);

        verify(tokenService).getUserByAuthToken(authToken);
        verify(fileRepository).save(any(FileEntity.class));
    }

    @Test(expected = ResponseStatusException.class)
    public void testUploadFileUnauthorized() {
        String authToken = "token";
        String filename = "file.txt";
        MultipartFile file = new MockMultipartFile("file", filename, "text/plain", "test data".getBytes());

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(null);

        fileService.uploadFile(authToken, filename, file);
    }

    @Test
    public void testDeleteFileSuccess() {
        String authToken = "token";
        String filename = "file.txt";
        User user = new User();
        user.setId(1L);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findByFilename(filename)).thenReturn(fileEntity);

        fileService.deleteFile(authToken, filename);

        verify(tokenService).getUserByAuthToken(authToken);
        verify(fileRepository).findByFilename(filename);
        verify(fileRepository).deleteByFilename(filename);
    }

    @Test(expected = ResponseStatusException.class)
    public void testDeleteFileUnauthorized() {
        String authToken = "token";
        String filename = "file.txt";

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(null);

        fileService.deleteFile(authToken, filename);
    }

    @Test(expected = ResponseStatusException.class)
    public void testDeleteFileNotFound() {
        String authToken = "token";
        String filename = "file.txt";
        User user = new User();
        user.setId(1L);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findByFilename(filename)).thenReturn(null);

        fileService.deleteFile(authToken, filename);
    }

    @Test
    public void testDownloadFileSuccess() {
        // Настройка тестовых данных
        String authToken = "token";
        String filename = "file.txt";
        User user = new User();
        user.setId(1L);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        byte[] fileData = "test data".getBytes();
        fileEntity.setData(fileData);

        // Настройка поведения методов getUserByAuthToken и findByFilename
        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findByFilename(filename)).thenReturn(fileEntity);

        // Вызов тестируемого метода
        byte[] result = fileService.downloadFile(authToken, filename);

        // Проверка результатов
        verify(tokenService).getUserByAuthToken(authToken);
        verify(fileRepository).findByFilename(filename);

        assertEquals(fileData, result);
    }

    @Test(expected = ResponseStatusException.class)
    public void testDownloadFileUnauthorized() {
        String authToken = "token";
        String filename = "file.txt";

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(null);

        fileService.downloadFile(authToken, filename);
    }

    @Test
    public void testEditFileNameSuccess() {
        String authToken = "token";
        String filename = "file.txt";
        String newFilename = "new-file.txt";
        User user = new User();
        user.setId(1L);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findByFilename(filename)).thenReturn(fileEntity);

        fileService.editFileName(authToken, filename, newFilename);

        verify(tokenService).getUserByAuthToken(authToken);
        verify(fileRepository).findByFilename(filename);
        verify(fileRepository).save(any(FileEntity.class));

        assertEquals(newFilename, fileEntity.getFilename());
    }

    @Test(expected = ResponseStatusException.class)
    public void testEditFileNameUnauthorized() {
        String authToken = "token";
        String filename = "file.txt";
        String newFilename = "new-file.txt";

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(null);

        fileService.editFileName(authToken, filename, newFilename);
    }

    @Test(expected = ResponseStatusException.class)
    public void testEditFileNameNotFound() {
        String authToken = "token";
        String filename = "file.txt";
        String newFilename = "new-file.txt";
        User user = new User();
        user.setId(1L);

        when(tokenService.getUserByAuthToken(authToken)).thenReturn(user);
        when(fileRepository.findByFilename(filename)).thenReturn(null);

        fileService.editFileName(authToken, filename, newFilename);
    }
}
