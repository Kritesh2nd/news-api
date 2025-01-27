package com.exm.news.controller;

import java.util.List;

import com.exm.news.service.NewsViewServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.exm.news.constant.PathConstant;
import com.exm.news.dto.user.GeneralUserDto;
import com.exm.news.dto.user.UpdateAuthorityDto;
import com.exm.news.dto.user.UpdateUserDto;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.service.UserServiceImplement;

import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping(PathConstant.USER)
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserServiceImplement userService;

    @Autowired
    private NewsViewServiceImpl newsViewService;

    @GetMapping(PathConstant.CAT)
    public ResponseEntity<String> cat() {
        return new ResponseEntity<String>("Cat User", HttpStatus.OK);
    }

    //	@PreAuthorize("hasAnyAuthority('admin','editor')")
    @GetMapping(PathConstant.LIST)
    public ResponseEntity<List<GeneralUserDto>> userList() {
        return new ResponseEntity<List<GeneralUserDto>>(userService.getGeneralUserList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping(PathConstant.GET_BY_ID)
    public ResponseEntity<GeneralUserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<GeneralUserDto>(userService.getGeneralUserById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin','editor','reader')")
    @PostMapping(PathConstant.UPDATE)
    public ResponseEntity<BasicResponseDto> updateUser(@RequestBody UpdateUserDto user) {
        return new ResponseEntity<BasicResponseDto>(userService.updateUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(PathConstant.UPDATE_ROLE)
    public ResponseEntity<BasicResponseDto> updateUserRole(
            @RequestBody UpdateAuthorityDto userAuthority) {
        return new ResponseEntity<BasicResponseDto>(
                userService.updateUserAuthority(userAuthority), HttpStatus.OK
        );
    }

    @PreAuthorize("hasAnyAuthority('reader')")
    @PostMapping(PathConstant.DELETE_ME)
    public ResponseEntity<BasicResponseDto> deleteMyAccount() {
        return new ResponseEntity<BasicResponseDto>(userService.deleteMyAccount(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping(PathConstant.DELETE_ROLE)
    public ResponseEntity<BasicResponseDto> deleteUserRole(@RequestBody UpdateAuthorityDto userAuthority) {
        return new ResponseEntity<BasicResponseDto>(userService.removeUserAuthority(userAuthority), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('admin','editor','reader')")
    @PostMapping("add-article-to-news-view")
    public ResponseEntity<BasicResponseDto> addArticleToNewsView(@RequestParam Long articleId, @RequestParam String side) {
        BasicResponseDto basicResponseDto = newsViewService.addToNewsView(articleId, side);
        return new ResponseEntity<BasicResponseDto>(basicResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin','editor','reader')")
    @PostMapping("remove-article-from-news-view")
    public ResponseEntity<BasicResponseDto> removeArticleFromNewsView(@RequestParam Long articleId, @RequestParam String side) {
        BasicResponseDto basicResponseDto = newsViewService.removeFromNewsView(articleId, side);
        return new ResponseEntity<BasicResponseDto>(basicResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin','editor','reader')")
    @PostMapping("request-editor-role")
    public ResponseEntity<BasicResponseDto> requestEditorRole() {
        BasicResponseDto basicResponseDto = userService.requestEditorAccess();
        return new ResponseEntity<>(basicResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("request-editor-list")
    public ResponseEntity<List<GeneralUserDto>> requestEditorList() {
        return new ResponseEntity<List<GeneralUserDto>>(userService.generalUserDtoListByRoleRequestEditor(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("testt", HttpStatus.OK);
    }



}
