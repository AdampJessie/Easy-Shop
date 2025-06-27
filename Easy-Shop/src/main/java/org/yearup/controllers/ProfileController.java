package org.yearup.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping("")
    Profile getUserProfile(Principal principal){
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);

        return profileDao.getByUserId(user.getId());
    }

    @PutMapping("")
    void update(Principal principal, @RequestBody Profile updateProfile){

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);

        updateProfile.setUserId(user.getId());

        profileDao.update(updateProfile);
    }


}
