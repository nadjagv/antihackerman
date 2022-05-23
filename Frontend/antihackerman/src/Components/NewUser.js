import {
  Modal,
  Box,
  Typography,
  TextField,
  Stack,
  Button,
  Checkbox,
  Autocomplete,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

const roles = ["Owner", "Tenant"];

function NewUser(props) {
  let open = props.modal;
  if (open === undefined) {
    open = false;
  }
  const [username, setUserame] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState(roles[2]);
  const [password, setPassword] = useState("");
  const [userObjects, setUserObjects] = useState([]);
  const [repPassword, setRepPassword] = useState("");
  const [options, setOptions] = useState([]);
  const closeModal = () => {
    props.close(false);
  };

  axios.defaults.withCredentials = true

  useEffect(() => {
    setOptions(props.objects);
  }, [props]);

  const registerUser = () => {
    console.log(userObjects)

    let validPassword = new RegExp(
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    if (
      password === "" ||
      repPassword === "" ||
      email === "" ||
      username === "" ||
      role === ""
    ) {
      alert("Emtpy fields");
      return;
    }
    if (!validPassword.test(password)) {
      alert(
        "Password must have upper and lower case, special character and number. It must be at least 8 characters long."
      );
      return;
    }
    if (password !== repPassword) {
      alert("Passwords do not match");
    }
    let roles;
    if (role === "Tenant") {
      roles = ["ROLE_TENANT"];
    }
    if (role === "Owner") {
      roles = ["ROLE_TENANT", "ROLE_OWNER"];
    }
    let registration = {
      password: password,
      username: username,
      email: email,
      roles: roles,
      groupId: props.group,
      realestate_ids: userObjects.map(object=>object.id),
    };

    console.log(registration)
    axios.post(environment.baseURL + "users", registration).then((response) => {
      closeModal();
    });
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
          Add object
        </Typography>
        <Stack spacing="3" alignItems="center">
          <TextField
            id="outlined-username-input"
            label="Username"
            type="text"
            onChange={(e) => setUserame(e.target.value)}
            value={username}
          />
          <TextField
            id="outlined-email-input"
            label="Email"
            type="text"
            onChange={(e) => setEmail(e.target.value)}
            value={email}
          />
          <TextField
            id="outlined-password-input"
            label="Password"
            type="password"
            onChange={(e) => setPassword(e.target.value)}
            value={password}
          />
          <TextField
            id="outlined-reppassword-input"
            label="Repeat password"
            type="password"
            onChange={(e) => setRepPassword(e.target.value)}
            value={repPassword}
          />
          <Autocomplete
            value={role}
            options={roles}
            disableClearable={true}
            sx={{ width: 225 }}
            renderInput={(params) => <TextField {...params} label="Role" />}
            onChange={(event, newValue) => {
              setRole(newValue);
            }}
          ></Autocomplete>
          {role==="Tenant" && <Autocomplete
            multiple
            id="checkboxes-tags-objects"
            value={userObjects}
            options={options.map((o) => o)}
            disableCloseOnSelect
            disableClearable={true}
            onChange={(e, newValue) => {
              setUserObjects(newValue);
            }}
            getOptionLabel={(option) => option.name}
            renderOption={(props, option, { selected }) => (
              <li {...props}>
                <Checkbox style={{ marginRight: 8 }} checked={selected} />
                {option.name}
              </li>
            )}
            style={{ width: 500 }}
            renderInput={(params) => (
              <TextField {...params} label="Objects" placeholder="Objects" />
            )}
          />}
          <Button
            onClick={() => {
              registerUser();
            }}
            variant="contained"
            color="success"
          >
            Create user
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
}
export default NewUser;
