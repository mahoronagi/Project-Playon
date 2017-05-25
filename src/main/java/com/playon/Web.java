package com.playon;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

@Controller
public class Web {

    String dbUrl = "jdbc:mysql://localhost/playon365?characterEncoding=UTF-8";
    String dbUser = "root";
    String dbPassword = "1234";

    Web() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @RequestMapping("/")
    String showHome(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", m.user);
        return "index";
    }

    @RequestMapping("/index")
    String showHomeIndex(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", m.user);
        return "index";
    }

    @RequestMapping("/login")
    String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String checkLogin(HttpSession session, String email, String password) {
        boolean passed = false;
        String sql = "select * from member where user=? and password=sha2(?,512)";
        try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, email);
            p.setString(2, password);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                passed = true;
                Member m = new Member();
                m.id = r.getLong("id");
                m.user = r.getString("user");
                m.role = r.getLong("role");
                session.setAttribute("member", m);
                System.out.println(" Log In Passed");
            } else {
                System.out.println("Log In Failed");
            }
            r.close();
            p.close();
            c.close();
        } catch (Exception e) {
        }
        if (passed) {
            return "redirect:/index";
        } else {
            return "redirect:/login?error=Invalid Email or Password";
        }
    }

    @RequestMapping("/logout")
    String showLogout(HttpSession session) {
        session.removeAttribute("member");
        return "redirect:login";
    }

    @RequestMapping("/profile")
    String showProfile(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
                model.addAttribute("role", m.role);
		model.addAttribute("member", m);
        return "profile";
    }
    
    @RequestMapping("/edit")
    String showEdit(HttpSession session, Model model){
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", m.user);
        return "edit";
    }
    
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    String showEditUser(HttpSession session, Model model, String password, String newpass){
        Member m = (Member)session.getAttribute("member");
        if (m == null) {
			return "redirect:/login";
		}
        model.addAttribute("user", m.user);
        System.out.println("password = "+password+" new password = "+newpass);
        if (m == null) {
			return "redirect:/login";
		}
        boolean passed = false;
        String sql = "select * from member where user=? and password=sha2(?,512)";
        try{
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, m.user);
            p.setString(2, password);
            ResultSet r = p.executeQuery();
            if (r.next()) { 
                String sqlu = "update member set password=sha2(?,512) where user=? and password=sha2(?,512)";
                PreparedStatement u = c.prepareStatement(sqlu);
                u.setString(1, newpass);
                u.setString(2, m.user);
                u.setString(3, password);
                u.execute();
                System.out.println("Insert In Passed");
                passed = true;
            } else {
                System.out.println("Insert In Failed");
            }
            r.close();
            p.close();
            c.close();
        } catch (Exception e) { } 
        if(passed){
           return "redirect:/profile"; 
        }else{
            return "edit";
        }
    }
    
    @RequestMapping("/admin")
    String showAdmin(HttpSession session, Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member) session.getAttribute("member");
        if (m == null) {
            return "redirect:/login";
        }
        try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("select * from member");
            while (r.next()) {
                Member p = new Member();
                p.id = r.getLong("id");
                p.user = r.getString("user");
                p.role = r.getLong("role");
                a.add(p);
            }
            r.close();
            s.close();
            c.close();
        } catch (Exception e) {
        }
        model.addAttribute("user", m.user);
        model.addAttribute("member", a);
        return "admin";
    }
    
    @RequestMapping("/newadmin")
    String showNewAdmin(HttpSession session, Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}     
        try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("select * from role");
            while (r.next()) {
                Role role = new Role();
                role.roleID = r.getInt("id");
                role.roleName = r.getString("role_name");
                a.add(role);
            }
            r.close();
            s.close();
            c.close();
        } catch (Exception e) {
        }       
        model.addAttribute("user", m.user);
        model.addAttribute("roles", a);
        return "newadmin";
    }
    
    @RequestMapping(value="/newadmin", method = RequestMethod.POST)
    String showNewAdminIns(HttpSession session, Model model, String user, String password, int role) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                /*
        try {
            String sql = "insert into member(user, password, role, time_create, time_edit) " 
                       + "values(?,?,?,now(), now())";
            Connection c = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            PreparedStatement p = c.prepareStatement(sql);
            p.setString(1, user);
            p.setString(2, password);
            p.setInt(3, role);
            p.execute();
            p.close(); c.close();
		} catch (Exception e) { }       */    
        return "admin";
    }

    @RequestMapping("/provider")
    String showProvider(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "provider";
    }

    @RequestMapping("/package")
    String showPackage(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "package";
    }

    @RequestMapping("/channel")
    String showChannel(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "channel";
    }

    @RequestMapping("/schedule")
    String showSchedule(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "schedule";
    }
    
    @RequestMapping("/reports")
    String showReports(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "reports";
    }

    @RequestMapping("/state")
    String showState(HttpSession session,Model model) {
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                model.addAttribute("user", m.user);
        return "state";
    }
}
