import React, { useState, useEffect } from "react";
import { Button, Result } from "antd";
import axios from "axios";
import NavBar from "../../components/NavBar";
import { Spin } from "antd";
import { Link } from "react-router-dom";
import FooterPart from '../../components/FooterPart'

const AddSuccess = () => {
  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");
  const [transaction, setTransaction] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/auth/getTransaction?id=${id}`)
      .then((response) => {
        console.log(response.data);
        setTransaction(response.data.payload);
        setIsLoading(true);
      });
  }, []);

  return (
    <div>
      <Spin spinning={!isLoading} fullscreen />
      <NavBar />
      {isLoading && (
        <div className="bg-gray-100 mx-auto max-w-screen-xl p-4">
        <div className="bg-gray-100 mt-40">
          <Result
            status="success"
            title="Hoàn thành giao dịch"
            subTitle={`Mã giao dịch : ${transaction.transactionId}`}
            extra={[
              <Link to="/addInputMoney" key="console">
              <Button type="primary" >
                  Add More               
              </Button>
              </Link> ,
              <Link key="buy" to="/home">
                <Button >Back To Home</Button>,              
              </Link>
            ]}
          />
        </div>
      </div>
      )}   
      <FooterPart />
    </div>
  );
};

export default AddSuccess;
