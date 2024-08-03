import axios from "axios";
import { Dropdown } from "flowbite-react";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import logo from "../assets/logo.png";
import { navLinks } from "../constants";

const NavBar = () => {
  const [isSticky, setIsSticky] = useState(false);
  const token = localStorage.getItem("token");
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [customer, setCustomer] = useState({});

  console.log(token);

  useEffect(() => {
    const handleScroll = () => {
      if (Window.scrollY > 100) {
        setIsSticky(true);
      } else {
        setIsSticky(false);
      }
    };
    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  });

  useEffect(() => {
    const fetchData = async () => {
      const customerResponse = await axios.get(
        "http://localhost:8080/api/auth/user/profile",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setCustomer(customerResponse.data.payload);
    };
    fetchData();
  }, []);

  console.log(customer);
  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
    window.location.href = "/";
  };

  useEffect(() => {
    setIsLoggedIn(token != null);
  }, [token]);

  const handleRequestToBecomeCreator = () => {
    Swal.fire({
      icon: "success",
      title: "Confirmation",
      text: "Do you want to request to become a creator?",
      showCancelButton: true,
      confirmButtonText: "Yes, I do",
      cancelButtonText: "Cancel",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .post(`http://localhost:8080/api/auth/requestBecomeCreator?Email=${customer.emailAddress}`, null, {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then((response) => {
            if (response.status === 200) {
              Swal.fire({
                position: "center",
                icon: "success",
                title: `Done`,
                html: "<h3>Send Successfully</h3>",
                showConfirmButton: false,
                timer: 1600,
              });
            } else {
              throw new Error(response.statusText);
            }
          })
          .catch((error) => {
            Swal.fire({
              icon: "error",
              title: "Oops...",
              text: "Something went wrong!",
              footer: '<a href="/">Try again!</a>',
            });
          });
      }
    });
  };

  return (
    <header className="w-full bg-gray-100 fixed top-0 left-0 right-0">
      <nav className="navbar px-10">
        <div className="space-x-10 h-[90px] flex justify-between items-center">
          <a href="/home">
            <img src={logo} className="w-[124px] h-[124px]" alt="Logo" />
          </a>

          <ul className="list-none space-x-16 sm:flex hidden justify-center items-center flex-1 font-semibold">
            {navLinks.map(({ link, path }) => (
              <Link to={`${path}`} key={path} className="block text-base">
                {link}
              </Link>
            ))}
          </ul>

          <div className="bg-gray-200 rounded-full sm:flex hidden items-center px-4 lg:w-[750px] ">
          </div>

          <div className="lg:flex items-center bg-[#2f6a81] rounded-lg">
            {token != null ? (
              <>
                <Dropdown label="Profile">
                  <Dropdown.Header>
                    <span className="block text-sm"></span>
                    <span className="block truncate text-sm font-medium"></span>
                  </Dropdown.Header>
                  <Dropdown.Item>
                    <Link
                      to="/viewEwallet"
                      className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                    >
                      My Wallet
                    </Link>
                  </Dropdown.Item>
                  <Dropdown.Item>
                    <Link
                      to="/requestHistory"
                      className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                    >
                      Request History
                    </Link>
                  </Dropdown.Item>
                  <Dropdown.Item>
                    <Link
                      to="/processRequest"
                      className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                    >
                      Get your pre-orders
                    </Link>
                  </Dropdown.Item>
                  {customer.roleName == "AUDIENCE" && (
                    <Dropdown.Item>
                      <Link
                        onClick={handleRequestToBecomeCreator}
                        className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                      >
                        Request To Become Creator
                      </Link>
                    </Dropdown.Item>
                  )}
                  {customer.roleName == "CREATOR" && (
                    <>
                      <Dropdown.Item>
                        <Link
                          to="/viewPreordersByCreator"
                          className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                        >
                          View your request orders
                        </Link>
                      </Dropdown.Item>

                      <Dropdown.Item>

                      </Dropdown.Item>
                    </>
                  )}
                  <Dropdown.Divider />
                  <Dropdown.Item>
                    <div
                      onClick={handleLogout}
                      className="cursor-pointer font-semibold lg:flex items-center hover:text-[#2f6a81]"
                    >
                      Sign out
                    </div>
                  </Dropdown.Item>
                </Dropdown>
              </>
            ) : (
              <>
                <a
                  href="/"
                  className="font-semibold lg:flex items-center hover:text-[#2f6a81]"
                >
                  Login
                </a>
                <Link
                  to="/register"
                  className="text-white bg-[#2f6a81] py-2 px-4 transition-all duration-300 rounded-full"
                >
                  Sign Up
                </Link>
              </>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
};

export default NavBar;
