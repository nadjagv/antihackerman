import { Modal, Box, Typography, TextField, Stack } from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

function EditUser(props) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  useEffect(() => {
    setUsername(props.user.username);
    setEmail(props.user.email);
  }, [props]);
  if (props.user) {
  }
  const closeModal = () => {
    props.close(false);
  };
  return (
    <Modal
      open={props.modal}
      onClose={() => {
        closeModal();
      }}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Edit user
        </Typography>
        <Stack spacing="3" alignItems="center">
          <TextField
            id="outlined-username-input"
            disabled={true}
            label="Username"
            type="text"
            onChange={(e) => setUsername(e.target.value)}
            value={username}
          />
          <TextField
            id="outlined-email-input"
            label="Email"
            type="text"
            onChange={(e) => setEmail(e.target.value)}
            value={email}
          />
        </Stack>
      </Box>
    </Modal>
  );
}
export default EditUser;
