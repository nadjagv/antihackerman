import {
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableCell,
  TableBody,
  Button,
  Stack,
  Modal,
  Box,
  Typography,
  Autocomplete,
  TextField,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import Header from "./Header";
import AuthService from "../Services/AuthService";

function Device() {
  const navigation = useNavigate();
  const { id } = useParams();

  axios.defaults.withCredentials = true;

  useEffect(() => {}, []);
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Stack></Stack>
    </div>
  );
}
export default Device;
