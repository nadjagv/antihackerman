import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import { Link } from "react-router-dom";
import AuthService from "../Services/AuthService";
import axios from "axios";
import environment from "../Constants/Environment";

function Header() {
  const [role, setRole] = useState("");

  axios.defaults.withCredentials = true
  
  const handleLogout = () => {
    axios.post(environment.baseURL + "auth/logout").then((response) => {
      AuthService.removeUser();
    });
  };
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link style={{ textDecoration: "none" }} to="/">
              Home
            </Link>
          </Typography>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link style={{ textDecoration: "none" }} to="/groups">
              Groups
            </Link>
          </Typography>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link style={{ textDecoration: "none" }} to="/certificates">
              Certificates
            </Link>
          </Typography>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link style={{ textDecoration: "none" }} to="/CSRs">
              CSRs
            </Link>
          </Typography>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link style={{ textDecoration: "none" }} to="/newCSR">
              New CSR
            </Link>
          </Typography>
          <Typography
            color="inherit"
            variant="h6"
            component="div"
            sx={{ flexGrow: 1 }}
          >
            <Link
              style={{ textDecoration: "none" }}
              to="/login"
              onClick={handleLogout}
            >
              Logout
            </Link>
          </Typography>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
export default Header;
