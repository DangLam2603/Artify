import React, { useState, useEffect } from "react";
import { Avatar, Button, Card, Modal } from "flowbite-react";
import { Link } from "react-router-dom";
import CommentBar from "../../components/CommentBar";
import { MdOutlineFileDownload } from "react-icons/md";
import { AiOutlineUserAdd } from "react-icons/ai";
import { Carousel } from "flowbite-react";
import { Spin } from "antd";
import { Watermark } from "antd";
import NavBar from "../../components/NavBar";
import axios from "axios";
import { FaRegHeart, FaHeart, FaRegCommentDots } from "react-icons/fa";
const PreviewPost = () => {
  const [p, setPost] = useState();
  const [openModal, setOpenModal] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null);
  const params = new URLSearchParams(window.location.search);
  const postId = params.get("postId");
  const myHeaders = new Headers();
  const token = localStorage.getItem("token");
  myHeaders.append("Authorization", `Bearer ${token}`);
  const requestOptions = {
    method: "GET",
    headers: myHeaders,
    redirect: "follow",
  };
  const handleImageClick = (imagePath) => {
    setSelectedImage(imagePath);
    setOpenModal(true);
  };
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    const fetchFData = () => {
      fetch(
        `http://localhost:8080/api/auth/getPostById?postId=${postId}`,
        requestOptions
      )
        .then((response) => {
          if (response.ok) {
            return response.json();
          }
          throw Error(response.statusText);
        })
        .then((result) => {
          console.log(result.payload);
          setPost(result.payload);
          setIsLoading(true);
        })
        .catch((error) => console.error(error));
    };
    fetchFData();
  }, []);
  return (
    <>
      <NavBar />
      <div className="bg-gray-100 mx-auto max-w-screen-xl p-4">
        <div className="bg-gray-100 mt-40 min-h-screen">
          <h1 className="text-3xl text-[#2f6a81] text-center font-semibold m-5">
            View Post
          </h1>
          <div className="flex flex-col justify-center items-center">
            {p != null || p != undefined ? (
              <Card
                key={p.postId}
                className="flex justify-center bg-white mb-5 w-full"
              >
                <div className="flex flex-row gap-6">
                  <div className="flex flex-col w-[65%] ">
                    <div className="flex justify-between gap-3">
                      <Link to="/artistProfile">
                        <Avatar rounded>
                          <div className="space-y-1 dark:text-white">
                            <div className="font-medium">{p.creatorName}</div>
                            <div className="text-sm text-gray-500 dark:text-gray-400">
                              {p.artList.map((item) => item.createdDate)}
                            </div>
                          </div>
                        </Avatar>
                      </Link>
                      <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1">
                        <AiOutlineUserAdd
                          size={20}
                          style={{ color: "#fff", fontWeight: "bold" }}
                        />
                        <button type="submit">Follow</button>
                      </div>
                    </div>
                    <div>
                      <p
                        className="text-sm my-2"
                        style={{
                          maxHeight: "3.6em",
                          width: "800px",
                          overflow: "hidden",
                          textOverflow: "ellipsis",
                          display: "-webkit-box",
                          WebkitLineClamp: 2,
                          WebkitBoxOrient: "vertical",
                        }}
                      >
                        {p.description}
                      </p>

                      <div className="w-full h-screen max-h-[50vh]">
                        <Carousel className="w-full mx-auto">
                          {p.artList.map((item, index) => (
                            <div
                              key={index}
                              onClick={() => handleImageClick(item)}
                            >
                              <img
                                src={item.imagePath}
                                className="rounded-md w-[800px] mx-auto"
                                alt={`Post Image - ${p.description}`}
                              />
                            </div>
                          ))}
                        </Carousel>
                      </div>
                    </div>

                    <div className="mt-5 flex justify-between">
                      <div className="flex justify-between gap-16">
                        <button className="flex gap-2 items-center">
                          <FaHeart
                            size={20}
                            style={{ color: "red", fontWeight: "bold" }}
                          />
                          0
                        </button>

                        <button className="flex gap-2 items-center">
                          <FaRegCommentDots
                            size={20}
                            style={{ color: "#000", fontWeight: "bold" }}
                          />
                          0
                        </button>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-col items-center justify-start w-[35%] gap-[70px] p-10  bg-slate-200">
                    <CommentBar postId={postId} />
                  </div>
                </div>

                {selectedImage && (
                  <Modal
                    dismissible
                    className="mt-10 px-72"
                    size={1}
                    show={openModal}
                    onClose={() => setOpenModal(false)}
                  >
                    <Modal.Body className="flex justify-center items-center">
                      <div>
                        {selectedImage.type.toUpperCase() !== "FREE" ? (
                          <Watermark content="Artify" font={{ color: "#ccc" }}>
                            <div style={{ height: 500 }}>
                              <div className="">
                                <img
                                  src={selectedImage.imagePath}
                                  alt="Selected Image"
                                  className="w-[700px] h-[480px] mx-auto relative"
                                />
                                {selectedImage.type.toUpperCase() !== "FREE" && (
                                  <div
                                    className="flex items-center justify-center absolute top-0 left-0 w-1/5 rounded-br-md"
                                    style={{ backgroundColor: "#f2f2f2" }}
                                  >
                                    <img
                                      src="https://cdn-icons-png.freepik.com/256/1319/1319983.png"
                                      className="w-[50px] mr-2"
                                      alt="Left Top Image"
                                    />
                                    <span className="text-[#F4980A] font-bold">
                                      {" "}
                                      PREMIUM{" "}
                                    </span>
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
                                <div className="font-medium">
                                  {p.creatorName}
                                </div>
                                <div className="text-sm text-gray-500 dark:text-gray-400">
                                  {p.artList.map((item) => item.createdDate)}
                                </div>
                              </div>
                            </Avatar>
                          </Link>
                          <div className="flex gap-4">
                            <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1">
                              <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                strokeWidth={1.5}
                                stroke="currentColor"
                                className="w-6 h-6"
                              >
                                <path
                                  strokeLinecap="round"
                                  strokeLinejoin="round"
                                  d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                                />
                              </svg>
                              <button type="submit">Like</button>
                            </div>
                            <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1">
                              <AiOutlineUserAdd
                                size={20}
                                style={{ color: "#fff", fontWeight: "bold" }}
                              />
                              <button type="submit">Follow</button>
                            </div>

                            {selectedImage.type.toUpperCase() !== "FREE" ? (
                              <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#F4980A] px-4 transition-all duration-300 rounded-full my-1">
                                <MdOutlineFileDownload
                                  size={20}
                                  style={{ color: "#fff", fontWeight: "bold" }}
                                />
                                <button type="submit">Download</button>
                              </div>
                            ) : (
                              <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1">
                                <MdOutlineFileDownload
                                  size={20}
                                  style={{ color: "#fff", fontWeight: "bold" }}
                                />
                                <button type="submit">Download</button>
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    </Modal.Body>
                  </Modal>
                )}
                <Link to="/">
                 <Button>Back to home</Button>
                </Link>
               
              </Card>
              
            ) : (
              <Spin spinning={!isLoading} fullscreen />
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default PreviewPost;
