import React, { useEffect, useState } from "react";
import NavBar from "../../components/NavBar";
import { Button } from "antd";
import { Spin } from "antd";
import { IoWallet } from "react-icons/io5";
import axios from "axios";
import FooterPart from '../../components/FooterPart'

const AddMoneyInput = () => {
  const [inputMoney, setInputMoney] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [eWallet, setEWallet] = useState({});
  const token = localStorage.getItem("token");
  useEffect(() => {
    const fetchData = async () => {
      const ewalletResponse = await axios.get(
        "http://localhost:8080/api/auth/viewEwallet",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setEWallet(ewalletResponse.data.payload);
      if (ewalletResponse) setIsLoading(true);
    };
    fetchData();
  }, []);

  const handleAmountChange = (e) => {
    setInputMoney(e.target.value);
  };

  console.log(eWallet);
  const addToWallet = async () => {
  await  axios
      .post(`http://localhost:8080/api/auth/pay?input_money=${inputMoney}`)
      .then((response) => {
        console.log(response.data);
        window.location.href = response.data;
      })
      .catch((error) => {
        console.error("Error adding to wallet:", error);
      });
  };


  return (
    <div>
      <Spin spinning={!isLoading} fullscreen />
      <NavBar />
      <div className="bg-gray-100 mx-auto max-w-screen-xl p-4">
        <div className="bg-gray-100 mt-40 min-h-screen">
          <div className="flex bg-slate-200 rounded-md flex-col mx-auto max-w-4xl py-24 sm:py-8 justify-center items-center">
            <div
              className="divide-y divide-gray-100 bg-white mt-5 py-2 px-8 shadow-md shadow-gray-300 rounded-md mb-5"
              style={{ height: "200px", width: "395px" }}
            >
              <div className="flex flex-col my-4">
                <label
                  htmlFor="price"
                  className="mr-5 block text-sm font-medium leading-6 text-gray-900"
                >
                  Charge Money in
                </label>
                <div className="relative mt-2 rounded-md shadow-sm">
                  <div className="mr-5 pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                    <span className="mr-5 text-gray-500 sm:text-sm">
                      <IoWallet />
                    </span>
                  </div>
                  <div className="block bg-slate-200 rounded-md border-0 py-1.5 pl-14 pr-20 text-gray-900 ring-2 ring-inset ring-[#2f6a81] placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
                    <div>Ewallet</div>
                    <div className="font-medium">{eWallet.totalAmount} VND</div>
                  </div>
                </div>
                <div className="relative mt-2 rounded-md shadow-sm">
                  <div className="mr-5 pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                    <span className="mr-5 text-gray-500 sm:text-sm">VND</span>
                  </div>
                  <input
                    type="text"
                    name="price"
                    id="price"
                    className="block w-[330px] rounded-md border-0 py-1.5 pl-14 pr-20 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                    onChange={handleAmountChange}
                    value={inputMoney}
                  /> 
                </div>
                {inputMoney > 1 && inputMoney < 10000 ? <div>Charge more than 10kVND</div> : null}
              </div>
            </div>
            <div
              className="divide-y divide-gray-100 bg-white mt-5 py-2 px-8 shadow-md shadow-gray-300 rounded-md mb-5"
              style={{ width: "395px", height: "200px" }}
            >
              <div className="flex flex-col my-4">
                <label
                  htmlFor="price"
                  className="mr-5 block text-sm font-medium leading-6 text-gray-900"
                >
                  From payment gate
                </label>
                <div className="relative mt-2 rounded-md shadow-sm">
                  <div className="mr-5 flex justify-center items-center pointer-events-none absolute inset-y-0 left-0 pl-3">
                    <span className="mr-5  text-gray-500 sm:text-sm">
                      <img
                        src="/src/assets/vnpay.png"
                        className="w-[20px] h-[20px] object-contain "
                      />
                    </span>
                  </div>
                  <div className="block bg-slate-200 rounded-md border-0 py-1.5 pl-14 pr-20 text-gray-900 ring-2 ring-inset ring-[#2f6a81] placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
                    <div>VNPay</div>
                    <div className="font-medium">Free chargement</div>
                  </div>
                </div>
              </div>
            </div>
            <Button
              onClick={addToWallet}
              className="w-[395px] bg-[#2f6a81] text-white"
            >
              Charge
            </Button>
          </div>
        </div>
      </div>
      <FooterPart />
    </div>
  );
};

export default AddMoneyInput;
