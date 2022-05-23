import {
  Modal,
  Box,
  Typography,
  TextField,
  Stack,
  Autocomplete,
  Checkbox,
  Button,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

const roles = ["Owner", "Tenant"];

function EditUser(props) {
  //const [username, setUsername] = useState("");
  //const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [userObjects, setUserObjects] = useState([]);
  const [options, setOptions] = useState([]);

  axios.defaults.withCredentials = true

  useEffect(() => {
    console.log(props.user.roleForGroup)
    if(props.user.roleForGroup==='Tenant'){
      axios.get(environment.baseURL+'users/realestatesTenanting/'+props.user.id).then(response=>{
        const objects=[...response.data]
        const objects2=objects.filter(obj=> obj.groupId==props.group)
        setUserObjects(objects2)
      })
    }
    setOptions(props.objects);
    setRole(props.user.roleForGroup)
  }, [props]);
  const changeUser = () => {
    if(role==='Tenant'){
      let realestate_ids= userObjects.map(object=>object.id);
      if(realestate_ids.length===0){
        alert("Need to select at least 1 object!")
      }else{
        axios.put(environment.baseURL+'users/to-tenant/'+props.user.id+'/'+props.group,realestate_ids);
        props.close(false);
      }
    }else{
      let realestate_ids= options.map(object=>object.id);
      axios.put(environment.baseURL+'users/to-owner/'+props.user.id+'/'+props.group,realestate_ids);
      props.close(false);
    }
  };
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
            isOptionEqualToValue={(option, value) => option.id === value.id}
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
              changeUser();
            }}
            variant="contained"
            color="success"
          >
            Save changes
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
}
export default EditUser;
