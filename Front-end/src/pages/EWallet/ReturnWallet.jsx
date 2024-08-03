import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ReturnWallet = () => {
  const token = localStorage.getItem("token");
  const navigate = useNavigate();
  const params = new URLSearchParams(window.location.search);
  const amount = params.get("vnp_Amount");
  const bankCode = params.get("vnp_BankCode");
  axios.interceptors.request.use(
    (config) => {
      config.headers.Authorization = `Bearer ${token}`;
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
  useEffect(() => {
    axios
      .get(
        `http://localhost:8080/api/auth/addEwallet?vnp_Amount=${amount}&vnp_bank_code=${bankCode}&vnp_ResponseCode=00`
      )
      .then((response) => {
        console.log(response.data);
        navigate(`/addSuccess?id=${response.data.payload.transactionId}`);
      });
  });

  return <div>ReturnWallet</div>;
};

export default ReturnWallet;
