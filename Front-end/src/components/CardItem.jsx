import { Spin, Watermark } from "antd";
import axios from "axios";
import { saveAs } from "file-saver";
import { useNavigate } from "react-router-dom";
import { Avatar, Card, Carousel, Modal } from "flowbite-react";
import React, { useEffect, useState } from "react";
import { AiOutlineUserAdd } from "react-icons/ai";
import { FaHeart, FaRegCommentDots, FaRegHeart } from "react-icons/fa";
import { MdOutlineFileDownload } from "react-icons/md";
import { Link } from "react-router-dom";
import CommentBar from "./CommentBar";
import moment from 'moment';
import Swal from "sweetalert2";
const CardItem = () => {
  const [p, setPost] = useState();
  const [openModal, setOpenModal] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [orderDTO, setOrderDTO] = useState({
    totalPrice: 0,
    artwork: {
      artId: "",
    },
  });
  const navigate = useNavigate();
  const [isLike, setIsLike] = useState(false);
  const [numberOfLikes, setNumberOfLikes] = useState(0);

  const params = new URLSearchParams(window.location.search);
  const postId = params.get("postId");

  const handleImageClick = (imagePath) => {
    setSelectedImage(imagePath);
    setOpenModal(true);
  };

  const myHeaders = new Headers();
  const token = localStorage.getItem("token");
  myHeaders.append("Authorization", `Bearer ${token}`);
  const requestOptions = {
    method: "GET",
    headers: myHeaders,
    redirect: "follow",
  };

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
          setNumberOfLikes(result.payload.numberOfLikes);
        })
        .catch((error) => console.error(error));
    };
    const fetchIsLikedData = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/auth/interaction/view?postId=${postId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log(response.data.payload);
        if (response.data.payload === null) {
          setIsLike(false);
        } else {
          setIsLike(response.data.payload.isLiked);
        }
        console.log(isLike);
      } catch (error) {
        console.error('Error fetching:', error);
      }
    };
    fetchFData();
    fetchIsLikedData();
  }, [isLike]);

  if (p != undefined) {
    console.log(p);
  }

  const downloadArt = async (image) => {
    const response = await axios.get(
      `http://localhost:8080/api/auth/downloadArt?artId=${image.artId}`
    );
    saveAs(`${response.data.payload}`, `${image.artName}.jpg`);
  };



  const makeOrder = async (image) => {
    const updatedOrder = {
      totalPrice: image.price,
      artwork: {
        artId: image.artId,
      },
    };
    console.log(updatedOrder);
    try {
      const order = await axios.post(
          "http://localhost:8080/api/auth/order/add",
          updatedOrder,
          {
              headers: { Authorization: `Bearer ${token}` },
          }
      );
      console.log(order);
      const response = await axios.get(
          `http://localhost:8080/api/auth/downloadArt?artId=${order.data.payload.artwork.artId}`
      );
      saveAs(`${response.data.payload}`, `${order.data.payload.artwork.artName}.jpg`);
  } catch (error) {
      Swal.fire({
          position: "center",
          icon: "error",
          title: "Oops...",
          html: "<h3>Something went wrong!</h3>",
          showConfirmButton: false,
          timer: 1600,
      });
      navigate("/viewEwallet");
  }

  };

  const handleLike = async () => {
    console.log(postId);
    console.log(token);
    const response = await axios.post(
      `http://localhost:8080/api/auth/interaction/like?postId=${postId}`,
      null,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (response.status === 200) {
      setIsLike(!isLike)
    }
  };

  const numComments = p?.interactions.reduce(
    (total, interaction) => total + interaction.comments.length,
    0
  );

  return (
    <>
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
                          {p.publishDate}
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
                        <div key={index} onClick={() => handleImageClick(item)}>
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
                    <button className="flex gap-2 items-center" onClick={handleLike}>
                      {isLike === true ? <FaHeart
                        size={20}
                        style={{ color: "red", fontWeight: "bold" }}
                      /> : <FaRegHeart
                        size={20}
                        style={{ color: "#000", fontWeight: "bold" }}
                      />}
                      {p.numberOfLikes}
                    </button>

                    <button className="flex gap-2 items-center">
                      <FaRegCommentDots
                        size={20}
                        style={{ color: "#000", fontWeight: "bold" }}
                      />
                      {numComments}
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
                                  src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Simple_crown_icon.svg/1024px-Simple_crown_icon.svg.png"
                                  className="w-[40px] mr-2"
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
                            <div className="font-medium">{p.creatorName}</div>
                            <div className="text-sm text-gray-500 dark:text-gray-400">
                              {moment(p.publishDate).format('DD/MM/YYYY')}
                            </div>
                          </div>
                        </Avatar>
                      </Link>
                      <div className="flex gap-4">


                        <div className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1  ">
                          <AiOutlineUserAdd
                            size={20}
                            style={{ color: "#fff", fontWeight: "bold" }}
                          />
                          <button type="submit">Follow</button>
                        </div>

                        {selectedImage.type.toUpperCase() !== "FREE" ? (
                          <div className="flex gap-4">
                            <div
                              onClick={() => makeOrder(selectedImage)}
                              className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#F4980A] px-4 transition-all duration-300 rounded-full my-1"
                            >
                              <MdOutlineFileDownload
                                size={20}
                                style={{ color: "#fff", fontWeight: "bold" }}
                              />
                              <button type="submit">Download</button>
                            </div>
                            <div className="sm:flex gap-2 hidden items-center font-semibold text-[#2f6a81] border-2 border-[#2f6a81] bg-[#fff] px-4 transition-all duration-300 rounded-full my-1">
                              <span>
                                Price:
                              </span>
                              {p.artList.map((item) => item.price)} VND
                            </div>
                          </div>

                        ) : (
                          <div
                            onClick={() => downloadArt(selectedImage)}
                            className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1"
                          >
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
          </Card>
        ) : (
          <Spin spinning={!isLoading} fullscreen />
        )}
      </div>
    </>
  );
};

export default CardItem;
