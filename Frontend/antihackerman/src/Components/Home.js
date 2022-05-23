import React from "react";
import AuthService from "../Services/AuthService";
import { useNavigate } from "react-router-dom";

function Home() {
  const navigation = useNavigate();
  if (!AuthService.getUser) {
    navigation("/login");
  }

  return (
    <div>
      <img src="/binary-code-greenjpg.jpg"></img>
    </div>
  );
}
export default Home;
