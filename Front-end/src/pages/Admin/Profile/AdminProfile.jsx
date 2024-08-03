import axios from "axios";
import { Card } from "flowbite-react";
import { Button, Spin, Table } from "antd";
import { EditOutlined } from '@ant-design/icons';
import { Link } from "react-router-dom";
import React, { useEffect, useState } from "react";


const AdminProfile = () => {

    const [user, setUser] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const token = localStorage.getItem("token");
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(
                    "http://localhost:8080/api/auth/user/profile",
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                setUser(response.data.payload);
                if (response) setIsLoading(true);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
        fetchData();
    }, []);

    console.log(user)

    return (
        <div className='h-full bg-gray-100 py-10 px-28'>
            <Spin spinning={!isLoading} fullscreen />
            <Card className="w-full p-5">
                <div className="relative">
                    <Link to="/editAdminProfile">
                        <Button icon={<EditOutlined />} onClick={() => handleEditProfile(record)} className="absolute top-0 right-0">
                            Edit Profile
                        </Button>
                    </Link>
                    <div className="flex items-center">
                        <img
                            alt="Avatar"
                            height="100"
                            width="100"
                            src={user.imagePath}
                            className="rounded-full"
                        />
                        <div className="flex flex-col mx-10">
                            <h5 className="text-xl font-medium text-gray-900 dark:text-white capitalize">
                                {user.userName}
                            </h5>
                            <p>{user.emailAddress}</p>
                        </div>

                    </div>
                </div>

            </Card>

            <Card className="w-full mt-10">
                <div className="flex items-center">
                    <div className="flex gap-9 mx-10 w-[20%]">
                        <div className="flex flex-col">
                            <h5 className="text-gray-900 dark:text-white capitalize">Username:</h5>
                            <h5 className="text-gray-900 dark:text-white capitalize">Email:</h5>
                            <h5 className="text-gray-900 dark:text-white capitalize">Telephone:</h5>
                            <h5 className="text-gray-900 dark:text-white capitalize">Role:</h5>
                        </div>
                        <div className="flex flex-col">
                            <span className="font-semibold capitalize">{user.userName}</span>
                            <span className="font-semibold">{user.emailAddress}</span>
                            <span className="font-semibold">{user.telephone}</span>
                            <span className="font-semibold">{user.roleName}</span>
                        </div>
                    </div>

                </div>
            </Card>


        </div>
    )
}

export default AdminProfile