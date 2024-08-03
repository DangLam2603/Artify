
import axios from "axios";
import React, { Fragment, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import FooterPart from '../../components/FooterPart';
import Hero from '../../components/Hero';
import NavBar from '../../components/NavBar';
import PostCard from '../../components/PostCard';
import AdminHome from "../Admin/AdminHome";


const Home = () => {
  const token = localStorage.getItem("token");
  const navigate = useNavigate();
  const [user, setUser] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.get(
        "http://localhost:8080/api/auth/user/profile",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setUser(response.data.payload);
    }
    fetchData();
  }, []);
  console.log(user);

  return (
    <div className='w-full bg-gray-100'>
      <div className="flex justify-center items-center ">
        <div className="w-full">

          {(user.roleName == "AUDIENCE" || user.roleName == "CREATOR") &&
            <Fragment>
              <NavBar />
              <Hero />
              <div className='m-10 flex flex-col items-center justify-center'>
                <PostCard />
              </div>
              <FooterPart />
            </Fragment>
          }
          {user.roleName == "ADMIN" &&
            <Fragment>
              <AdminHome />
            </Fragment>
          }
        </div>
      </div>
    </div>

  )

}
export default Home;