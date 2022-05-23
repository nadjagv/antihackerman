import React from "react";
import AuthService from "../Services/AuthService";
import { useNavigate } from "react-router-dom";
import Header from "./Header";

function Home() {
  const navigation = useNavigate();
  if (!AuthService.getUser) {
    navigation("/login");
  }

  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <img src="/binary-code-greenjpg.jpg"></img>
    </div>
  );
}
export default Home;
