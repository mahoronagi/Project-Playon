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

    String dbUrl = "jdbc:mysql://104.155.230.50:3306/playon365?characterEncoding=UTF-8";
    String dbUser = "root";
    String dbPassword = "";

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
        } catch (Exception e) { }
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
    String showNewAdminIns(HttpSession session, Model model, String username, String password, int role) {
        Member m = (Member)session.getAttribute("member");
            if (m == null) {
                    return "redirect:/login";
            }
        try {
            String n ="";
            String sqlname = "select * from member where user = ?";
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement pn = c.prepareStatement(sqlname);
            pn.setString(1, username);
            ResultSet rn = pn.executeQuery();
            while (rn.next()) {
                n = rn.getString("user");
            }
            if(username.equals(n)){
                System.out.println("user same");
                model.addAttribute("user", m.user);
                return "redirect:/newadmin";
            }else{
                String sql = "insert into member(user, password, role) " 
                           + "values(?,sha2(?,512),?)";
                PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, username);
                p.setString(2, password);
                p.setInt(3, role);
                p.execute(); p.close(); c.close();
                System.out.println("OK insert");
            }
	} catch (Exception e) { }  
        model.addAttribute("user", m.user);
        return "redirect:/admin";
    }

    @RequestMapping("/provider")
    String showProvider(HttpSession session,Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
                try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("select * from provider");
            while (r.next()) {
                Provider p = new Provider();
                p.id = r.getInt("id");
                p.name = r.getString("provider_name");
                p.url = r.getString("url");
                p.domain = r.getString("domain");
                p.ip = r.getString("ip_host");
                p.type = r.getString("type");
                p.upackage = r.getString("package");
                p.DateS = r.getString("date_start");
                p.DateE = r.getString("date_end");
                a.add(p);
            }
            r.close();
            s.close();
            c.close();
        } catch (Exception e) { } 
        model.addAttribute("user", m.user);
        model.addAttribute("provider", a);
        return "provider";
    }
    
    @RequestMapping("/newprovider")
    String showNewProvider(HttpSession session, Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}     
        /*try {
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
        } catch (Exception e) { }    */   
        model.addAttribute("user", m.user);
        return "newprovider";
    }
    
    @RequestMapping(value="/newprovider", method = RequestMethod.POST)
    String showNewProviderIns(HttpSession session, Model model, 
        String provider_name, String domain, String ip, String url, String p_package, String type,
        String date_start, String date_end) {
        Member m = (Member)session.getAttribute("member");
        FormatDate d = new FormatDate();
            if (m == null) {
                    return "redirect:/login";
            }
        try {
            String n ="";
            String sqlname = "select * from provider where provider_name = ?";
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement pn = c.prepareStatement(sqlname);
            pn.setString(1, provider_name);
            ResultSet rn = pn.executeQuery();
            while (rn.next()) {
                n = rn.getString("provider_name");
            }
            if(provider_name.equals(n)){
                System.out.println("false");
                model.addAttribute("user", m.user);
                return "redirect:/newprovider";
            }else{
                String sql = "insert into provider(provider_name, domain, ip_host,"
                            + "url, package, type, date_start, date_end) " 
                           + "values(?,?,?,?,?,?,?,?)";
                PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, provider_name);
                p.setString(2, domain);
                p.setString(3, ip);
                p.setString(4, url);
                p.setString(5, p_package);
                p.setString(6, type);
                p.setString(7, d.formatDate(date_start));
                p.setString(8, d.formatDate(date_end));
                p.execute(); p.close(); c.close();
                System.out.println("OK insert");
            }
	} catch (Exception e) { System.out.println(e);}  
        return "redirect:/provider";
    }

    @RequestMapping("/package")
    String showPackage(HttpSession session,Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
        try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("select * from package");
            while (r.next()) {
                Package p = new Package();
                p.id = r.getInt("id");
                p.name = r.getString("package_name");
                a.add(p);
            }
            r.close();
            s.close();
            c.close();
        } catch (Exception e) { } 
        model.addAttribute("user", m.user);
        model.addAttribute("package", a);
        return "package";
    }

    @RequestMapping("/channels")
    String showChannel(HttpSession session,Model model) {
        ArrayList a = new ArrayList();
        Member m = (Member)session.getAttribute("member");
		if (m == null) {
			return "redirect:/login";
		}
        try {
            Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery("select * from channel");
            while (r.next()) {
                Channel p = new Channel();
                p.id = r.getLong("id");
                p.ChImg = r.getString("img");
                p.ChID = r.getLong("channel_id");
                p.name = r.getString("channel_name");
                p.code = r.getString("channel_code");
                a.add(p);
            }
            r.close();
            s.close();
            c.close();
        } catch (Exception e) { } 
        model.addAttribute("user", m.user);
        model.addAttribute("channel", a);
        return "channels";
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
