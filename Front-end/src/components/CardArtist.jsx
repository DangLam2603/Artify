import React, { useState, useEffect } from 'react'
import { Avatar, Card } from "flowbite-react";
import { AiOutlineUserAdd } from "react-icons/ai";
import axios from "axios";
import ModalComponent from "./ModalComponent";
import RequestArt from "./RequestArt";

const CardArtist = () => {

    const [artist, setArtist] = useState([]);
    const token = localStorage.getItem("token");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedCreatorId, setSelectedCreatorId] = useState(null);


    useEffect(() => {
        const fetchData = async () => {
            const response = await axios.get(
                "http://localhost:8080/api/auth/creatorList",
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            if (response) {
                setArtist(response.data.payload);
            }
        };
        fetchData();
    }, []);
    console.log("a", artist)

    const handleRequestClick = (creatorId) => {
        setSelectedCreatorId(creatorId);
        setIsModalOpen(true);
    };



    return (
        <div className='h-full'>
            <div className="h-full grid grid-cols-3 gap-28 sm:grid-cols-3 px-20 md:px-20 lg:px-40">
                {artist.length > 0 &&
                    artist.map((item, index) => (
                        <Card key={index} className='h-[280px]'>
                            <div className="flex flex-col items-center">
                                <Avatar rounded size="lg" />
                                <h5 className="my-4 text-xl font-medium text-gray-900 dark:text-white">{item.userName}</h5>
                                <span className="text-sm text-gray-500 dark:text-gray-400">
                                    Artist
                                </span>
                                <div className="mt-4 flex space-x-3 lg:mt-6">
                                    <div className="cursor-pointer font-semibold border-2 sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full  hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2">
                                        <AiOutlineUserAdd
                                            size={20}
                                            className="hover:text-[#2f6a81]"
                                        />
                                        <button type="submit">Follow</button>
                                    </div>
                                    <div className="cursor-pointer font-semibold border-2 sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full  hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2">
                                        <AiOutlineUserAdd
                                            size={20}
                                            className="hover:text-[#2f6a81]"
                                        />
                                        <button type="button" onClick={() => handleRequestClick(item.userId)}>Request</button>
                                    </div>
                                </div>
                            </div>
                        </Card>
                    ))}
            </div>

            <ModalComponent isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <RequestArt creatorId={selectedCreatorId} />
            </ModalComponent>
        </div>
    )
}

export default CardArtist