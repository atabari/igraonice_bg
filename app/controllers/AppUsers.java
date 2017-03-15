package controllers;

import helpers.Authenticator;
import helpers.Cookies;
import helpers.Session;
import helpers.UserAccessLevel;
import models.Apartment;
import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.allusers;
import views.html.createuser;
import views.html.userapartments;
import views.html.userpanel;

import java.util.List;

/**
 * Created by User on 1/29/2016.
 */
public class AppUsers extends Controller {
    final Integer userId = UserAccessLevel.getCurrentUser(ctx()).id;

            /* --------------- create user page render ---------------*/

    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result createUserRender(){
        return ok(createuser.render(userId));
    }

            /* --------------- create user ---------------*/

    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result createUser(){
        AppUser.createUser();
        return redirect(routes.AppUsers.listOfUsers());
    }
            /* --------------- list of all users ---------------*/

    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result listOfUsers(){
        List<AppUser> users = AppUser.getAllAppUsers();
        return ok(allusers.render(users, userId));
    }
            /* --------------- delete user ---------------*/

    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result deleteUser(Integer userId){
        AppUser.deleteUser(userId);
        List<AppUser> users = AppUser.getAllAppUsers();

        return status(200, allusers.render(users, userId));
    }
            /* --------------- logout user ---------------*/

    public Result logout(){
        Session.clearUserSessionData();
        Cookies.clearCookies();
        return redirect(routes.Application.index());
    }

                /* --------------- user panel render ---------------*/
//
//    public Result userPanelRender(String email){
//        AppUser user = AppUser.findUserByEmail(email);
//        return ok(userpanel.render(user));
//    }
    /* --------------- user apartments ---------------*/
    public Result userApartmentsRender(Integer userId){

        List<Apartment> apartments = Apartment.userApartments(userId);

        return ok(userapartments.render(apartments, userId));
    }



}
