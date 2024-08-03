import React, { useState, useEffect } from 'react'
import { Watermark } from 'antd';
import { Link } from "react-router-dom";
import { Avatar, Modal } from "flowbite-react";
import { AiOutlineUserAdd } from "react-icons/ai";
import { MdOutlineFileDownload } from "react-icons/md";
import axios from 'axios';
import moment from 'moment';

const ImageList = () => {
    const [post, setPost] = useState([]);
    const [gallery, setGallery] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [openModal, setOpenModal] = useState(false);
    const [indexImage, setIndexImage] = useState(0);

    let combineArt = [];

    const fetchData = async () => {
        try {
            const token = localStorage.getItem('token');
            const headers = {
                Authorization: `Bearer ${token}`
            };
            const response = await axios.get('http://localhost:8080/api/auth/audience/viewAllPosts', {
                headers
            });
            const result = response.data;
            console.log(result.payload);
            setPost(result.payload);
            if (result && result.payload && result.payload.length > 0) {
                setIsLoading(true);
            }
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        combineArt = post?.flatMap(p => p.artList)
        setGallery(combineArt)
    }, [post])

    const handleImageClick = (imagePath, index) => {
        setSelectedImage(imagePath);
        setOpenModal(true);
        setIndexImage(index)
    };


    return (
        <div className='pb-10 h-full'>
            <div className="h-full grid grid-cols-3 gap-16 sm:grid-cols-3 px-8 md:px-16 lg:px-28">
                {post?.length > 0 ?
                    gallery.map((p, postIndex) => (
                        <div key={postIndex} className="">
                            {
                                <div key={postIndex} onClick={() => handleImageClick(p, postIndex)}>
                                    <img
                                        src={p.imagePath}
                                        className="rounded-md w-[500px] h-[300px] mx-auto cursor-pointer"
                                        alt={`Post Image - ${p.description}`}
                                    />
                                </div>
                            }
                        </div>
                    ))
                    : null}
            </div>

            {selectedImage && (
                <Modal dismissible className='mt-10 px-72' size={1} show={openModal} onClose={() => setOpenModal(false)}>
                    <Modal.Body className='flex justify-center items-center'>
                        <div>
                            {selectedImage.type !== "FREE" ? (
                                <Watermark content="Artify" font={{ color: '#ccc' }}>
                                    <div style={{ height: 500 }}>
                                        <div className="">
                                            <img
                                                src={selectedImage.imagePath}
                                                alt="Selected Image"
                                                className="w-[700px] h-[480px] mx-auto relative"
                                            />
                                            {selectedImage.type !== "FREE" && (
                                                <div className='flex items-center justify-center absolute top-0 left-0 w-1/5 rounded-br-md' style={{ backgroundColor: '#f2f2f2' }}>
                                                    <img
                                                        src="https://cdn-icons-png.freepik.com/256/1319/1319983.png"
                                                        className="w-[50px] mr-2"
                                                        alt="Left Top Image"
                                                    />
                                                    <span className="text-[#F4980A] font-bold"> Premium </span>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </Watermark>
                            ) : (
                                <div style={{ height: 500 }}>
                                    <div className="">
                                        <img
                                            src={selectedImage.imagePath}
                                            alt="Selected Image"
                                            className="w-[700px] h-[480px] mx-auto relative"
                                        />
                                    </div>
                                </div>
                            )}

                            <div className="flex justify-between gap-3 w-[700px]">
                                <Link href="">
                                    <Avatar rounded>
                                        <div className="space-y-1 dark:text-white">
                                            <div className='font-medium'>{post[0].creatorName}</div>
                                            <div className="text-sm text-gray-500 dark:text-gray-400">
                                                {moment(selectedImage.publishDate).format('DD/MM/YYYY')}
                                            </div>
                                        </div>
                                    </Avatar>
                                </Link>
                                <div className='flex gap-4'>
                                    <div className='cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1'>
                                        <AiOutlineUserAdd size={20} style={{ color: '#fff', fontWeight: 'bold' }} />
                                        <button type='submit'>Follow</button>
                                    </div>

                                    {(selectedImage.type !== "FREE") ? (
                                        <div className='flex gap-4'>
                                            <div className='cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#F4980A] px-4 transition-all duration-300 rounded-full my-1'>
                                                <MdOutlineFileDownload size={20} style={{ color: '#fff', fontWeight: 'bold' }} />
                                                <button type="submit">Download</button>
                                            </div>
                                            <div className="sm:flex gap-2 hidden items-center font-semibold text-[#2f6a81] border-2 border-[#2f6a81] bg-[#fff] px-4 transition-all duration-300 rounded-full my-1">
                                                <span>
                                                    Price:
                                                </span>
                                                {selectedImage.price} VND
                                            </div>
                                        </div>

                                    ) : (
                                        <div className='cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1'>
                                            <MdOutlineFileDownload size={20} style={{ color: '#fff', fontWeight: 'bold' }} />
                                            <button type="submit">Download</button>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    </Modal.Body>
                </Modal>
            )}
        </div>
    )
}

export default ImageList