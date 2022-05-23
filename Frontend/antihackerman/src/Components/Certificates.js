import {
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableCell,
  TableBody,
  Button,
  Modal,
  Box,
  Typography,
  Autocomplete,
  TextField,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";
import Header from "./Header";
import AuthService from "../Services/AuthService";

const revocationReasons = [
  "AA_COMPROMISE",
  "AFFILIATION_CHANGED",
  "CA_COMPROMISE",
  "CERTIFICATE_HOLD",
  "CESSATION_OF_OPERATION",
  "KEY_COMPROMISE",
  "PRIVILEGE_WITHDRAWN",
  "REMOVE_FROM_CRL",
  "SUPERSEDED",
  "UNSPECIFIED",
  "UNUSED",
];

function Certificates() {
  const [certificates, setCertificates] = useState([]);
  const [revocationModalOpen, setRevocationModalOpen] = useState(false);
  const [detailsModal, setDetailsModal] = useState(false);
  const [certDetails, setCertDetails] = useState("");
  const [toRevokeSerial, setToRevokeSerial] = useState("");
  const [revocationReason, setRevocationReason] = useState(
    revocationReasons[0]
  );

  axios.defaults.withCredentials = true

  useEffect(() => {
    axios.get(environment.baseURL + "cert").then((response) => {
      setCertificates(response.data);
    });
  }, []);
  const revocationHandle = () => {
    axios
      .post(
        environment.baseURL +
          "cert/crl/" +
          toRevokeSerial +
          "/" +
          revocationReason
      )
      .then((response) => {
        setRevocationModalOpen(false);
      });
  };
  const getCerticateDetails = (alias) => {
    axios.get(environment.baseURL + "cert/" + alias).then((reponse) => {
      setCertDetails(reponse.data);
    });
  };
  const onValidityCheck = (alias) => {
    axios
      .get(environment.baseURL + "cert/validity/" + alias)
      .then((response) => {
        if (response.data) {
          alert("Certificate IS valid");
        } else {
          alert("Certificate is NOT valid");
        }
      });
  };
  const onRevocationCheck = (serial) => {
    axios.get(environment.baseURL + "cert/crl/" + serial).then((response) => {
      if (response.data) {
        alert("Certificate IS revoked");
      } else {
        alert("Certificate is NOT revoked");
      }
    });
  };
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Alias</TableCell>
              <TableCell>More details</TableCell>
              <TableCell>Check validity</TableCell>
              <TableCell>Check revocation</TableCell>
              <TableCell>Revoke certificate</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {certificates.map((cert) => (
              <TableRow key={cert.alias}>
                <TableCell>{cert.alias}</TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      getCerticateDetails(cert.alias);
                      setDetailsModal(true);
                    }}
                    variant="contained"
                  >
                    More details
                  </Button>
                </TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      onValidityCheck(cert.alias);
                    }}
                    variant="contained"
                  >
                    Validity
                  </Button>
                </TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      onRevocationCheck(cert.serial);
                    }}
                    variant="contained"
                  >
                    Revocation
                  </Button>
                </TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      setToRevokeSerial(cert.serial);
                      setRevocationModalOpen(true);
                    }}
                    variant="contained"
                    color="error"
                  >
                    Revoke
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Modal
        open={detailsModal}
        onClose={() => {
          setDetailsModal(false);
        }}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={modalStyle}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Certificate details
          </Typography>
          <div
            id="modal-modal-description"
            sx={{ mt: 2 }}
            className="modal-text"
          >
            {certDetails}
          </div>
        </Box>
      </Modal>
      <Modal
        open={revocationModalOpen}
        onClose={() => {
          setRevocationModalOpen(false);
        }}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={modalStyle}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Choose revocation reason
          </Typography>
          <Autocomplete
            value={revocationReason}
            onChange={(event, newValue) => {
              setRevocationReason(newValue);
            }}
            disablePortal
            id="combo-box-demo"
            options={revocationReasons}
            sx={{ width: 320 }}
            renderInput={(params) => <TextField {...params} label="Reason" />}
          />
          <Button
            onClick={revocationHandle}
            sx={{ mt: 2 }}
            variant="contained"
            color="success"
          >
            Revoke
          </Button>
        </Box>
      </Modal>
    </div>
  );
}
export default Certificates;
