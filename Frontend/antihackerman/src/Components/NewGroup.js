import {
    Modal,
    Box,
    Typography,
    TextField,
    Stack,
    Button,
  } from "@mui/material";
  import React from "react";
  import { useState } from "react";
  import axios from "axios";
  import environment from "../Constants/Environment";
  import modalStyle from "../Constants/Styles";
  
  function NewGroup(props) {
    let open = props.modal;
    if (open === undefined) {
      open = false;
    }
    const [name, setName] = useState("");
  
    axios.defaults.withCredentials = true
  
    const createGroup = () => {
      axios.post(environment.baseURL +'groups',name)
      props.close(false);
      window.location.reload();
    };
    const closeModal = () => {
      props.close(false);
    };
    return (
      <Modal
        open={open}
        onClose={() => {
          closeModal();
        }}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={modalStyle}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Add group
          </Typography>
          <Stack spacing="3" alignItems="center">
            <TextField
              id="outlined-name-input"
              label="Name"
              type="text"
              onChange={(e) => setName(e.target.value)}
              value={name}
            />
            <Button
              onClick={() => {
                createGroup();
              }}
              variant="contained"
              color="success"
            >
              Create group
            </Button>
          </Stack>
        </Box>
      </Modal>
    );
  }
  export default NewGroup;