import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import NavBar from "../../components/NavBar";
import { Link } from "react-router-dom";
import { Button } from "antd";
import { Spin } from "antd";
import { Avatar } from "antd";
import { IoMdSettings } from "react-icons/io";
import { IoWallet } from "react-icons/io5";
import { FaPlus } from "react-icons/fa";
import { Pagination } from "antd";
import { MdEmail } from "react-icons/md";
import { BsFillTelephoneFill } from "react-icons/bs";
import { MdOutlineModeEdit } from "react-icons/md";
import FooterPart from "../../components/FooterPart";

const ViewEwallet = () => {
  const navigate = useNavigate();
  const [amount, setAmount] = useState(0);
  const [inputMoney, setInputMoney] = useState("");
  const [customer, setCustomer] = useState({});
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [totalPage, setTotalPage] = useState(0);
  const [thisPage, setThisPage] = useState(1);
  const [eWallet, setEWallet] = useState({});
  const token = localStorage.getItem("token");
  // const formattedNumber = (number) => {
  //   return number.toLocaleString('en-US', { style: 'decimal' });
  // };
  const itemPerPage = 4;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const customerResponse = await axios.get(
          "http://localhost:8080/api/auth/user/profile",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setCustomer(customerResponse.data.payload);

        const ewalletResponse = await axios.get(
          "http://localhost:8080/api/auth/viewEwallet",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setEWallet(ewalletResponse.data.payload);

        const transactionsResponse = await axios.get(
          "http://localhost:8080/api/auth/viewTransactions",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setTransactions(transactionsResponse.data.payload);

        if (customerResponse && ewalletResponse && transactionsResponse) {
          setIsLoading(true);
          setTotalPage(
            Math.ceil(transactionsResponse.data.payload.length / itemPerPage)
          );
          console.log(ewalletResponse);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []);

  const indexOfLastTransaction = thisPage * itemPerPage;
  const indexOfFirstTransaction = indexOfLastTransaction - itemPerPage;

  const currentTransactions = transactions.slice(
    indexOfFirstTransaction,
    indexOfLastTransaction
  );

  console.log(transactions);
  console.log(isLoading);
  console.log(totalPage);
  console.log(customer);

  const handlePageClick = (data) => {
    setThisPage(data);
  };

  const stats = [
    { id: 1, name: "Number of transactions", value: transactions.length },
    { id: 2, name: "Account Balance", value: `${eWallet.totalAmount} VND` },
    // { id: 3, name: "New users annually", value: "46,000" },
  ];
  return (
    <div className="">
      <Spin spinning={!isLoading} fullscreen />
      <NavBar />

      {isLoading && (
        <div className="bg-gray-100 mx-auto max-w-screen-xl p-4">
          <div className="bg-gray-100 mt-32">
            {/* Banner intro */}
            <div
              className="flex justify-center items-center"
              style={{ marginBottom: "-10px" }}
            >
              <div
                style={{ width: "500px" }}
                className="justify-between h-15 inline-flex items-center rounded-md bg-white px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10 mb--20"
              >
                <div className="flex p-2 items-center">
                  <Avatar size="large" src={customer.imagePath} />
                  <p className="ml-4 text-base capitalize">{customer.userName}</p>
                </div>
                {customer.roleName == "CREATOR" && (
                <div className="flex p-2 items-center">
                  <IoMdSettings className="w-5 h-5 cursor-pointer" />
                  <p className="ml-2 text-base">Settings</p>
                    <>
                      <Link
                        to={`/viewMyPosts?creatorId=${customer.userId}`}
                        className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                      >
                        View your posts
                      </Link>
                      <Link
                        to={`/addArts`}
                        className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                      >
                        Add posts
                      </Link>
                    </>
                 
                </div>
                 )}
              </div>
            </div>

            <div className="mx-auto max-w-4xl  sm:px-6  lg:px-8 text-white bg-[#2f6a81] py-10 sm:py-10">
              <div className="mx-auto max-w-7xl px-6 lg:px-8">
                <dl className="grid grid-cols-2 gap-x-8 gap-y-16 text-white text-center lg:grid-cols-2">
                  {stats.map((stat) => (
                    <div
                      key={stat.id}
                      className="mx-auto flex max-w-xs flex-col gap-y-4"
                    >
                      <dt className="text-base leading-7 text-white">
                        {stat.name}
                      </dt>
                      <dd className="order-first text-2xl font-semibold tracking-tight text-white sm:text-3xl">
                        {stat.value}
                      </dd>
                    </div>
                  ))}
                </dl>
              </div>
            </div>

            <div
              className="but-container flex items-center justify-center"
              style={{ marginTop: "-10px" }}
            >
              <Link to="/addInputMoney">
                <Avatar
                  size="large"
                  className="bg-white text-[#2f6a81]"
                  icon={<FaPlus />}
                />
              </Link>
            </div>

            <div className="flex mx-auto max-w-4xl py-16 sm:py-4 justify-between">
              <div
                className="divide-y divide-gray-100 bg-white mt-5 py-2 px-8 shadow-md shadow-gray-300 rounded-md mb-5 mr-5"
                style={{ width: "300px", height: "300px" }}
              >
                <div style={{ width: "240px", height: "300px" }}>
                  <div className="font-semibold text-2xl text-center m-5">
                    Profile
                  </div>
                  <div className="flex my-8 items-center">
                    <MdEmail
                      size={20}
                      className="flex-none rounded-full bg-gray-50 mr-10"
                    />
                    <div>{customer.emailAddress}</div>
                  </div>
                  <div className="flex my-8 items-center">
                    <BsFillTelephoneFill
                      size={20}
                      className="flex-none rounded-full bg-gray-50 mr-10"
                    />
                    <div>{customer.telephone}</div>
                  </div>
                  <Link to="/editProfile">
                    <div className="cursor-pointer sm:flex gap-2 hidden items-center justify-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full my-1">
                      <MdOutlineModeEdit
                        size={20}
                        style={{ color: "#fff", fontWeight: "bold" }}
                      />
                      <button type="submit"> Edit Profile</button>
                    </div>
                  </Link>
                </div>
              </div>

              <div className="flex flex-col items-center justify-start">
                <ul
                  role="list"
                  className="divide-y divide-gray-100 bg-white mt-5 py-5 px-8 shadow-md shadow-gray-300 rounded-md mb-5"
                  style={{ width: "600px" }}
                >
                  <div className="font-semibold text-2xl">Transactions</div>

                  {transactions.length > 0
                    ? currentTransactions.map((transaction) => (
                        <li
                          key={transaction.id}
                          className="flex justify-between gap-x-6 py-5"
                        >
                          <div className="flex min-w-0 gap-x-4">
                            <IoWallet className="h-8 w-8 flex-none rounded-full bg-gray-50" />
                            <div className="min-w-0 flex-auto">
                              <p className="text-sm font-semibold leading-6 text-gray-900">
                                {transaction.name}
                              </p>
                              <p className="mt-1 truncate text-xs leading-5 text-gray-500">
                                {transaction.email}
                              </p>
                            </div>
                          </div>
                          <div className="hidden shrink-0 sm:flex sm:flex-col sm:items-end">
                            <p className="text-sm leading-6 text-gray-900">
                             {transaction.totalMoney > 0 ? `+ ${transaction.totalMoney}` : transaction.totalMoney }
                            </p>

                            <p className="mt-1 text-xs leading-5 text-gray-500">
                              At{" "}
                              <time dateTime={transaction.lastSeenDateTime}>
                                {transaction.transactionDate}
                              </time>
                            </p>
                          </div>
                        </li>
                      ))
                    : null}
                </ul>
                <Pagination
                  defaultCurrent={1}
                  current={thisPage}
                  pageSize={itemPerPage}
                  total={Math.ceil(transactions.length)}
                  onChange={handlePageClick}
                />
              </div>
            </div>
          </div>
        </div>
      )}

      <FooterPart />
    </div>
  );
};

export default ViewEwallet;
