import { Spin } from "antd";
import axios from "axios";
import { Avatar, Card, Carousel } from "flowbite-react";
import React, { useEffect, useState } from "react";
import { AiOutlineUserAdd } from "react-icons/ai";
import { FaHeart, FaRegCommentDots, FaRegHeart } from "react-icons/fa";
import { Link } from "react-router-dom";
import InputComment from "./InputComment";

const PostCard = () => {
  const [post, setPost] = useState([]);
  const [p, setP] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [customer, setCustomer] = useState({});
  const token = localStorage.getItem("token");
  const [isLike, setIsLike] = useState(false);
  const [numberOfLikes, setNumberOfLikes] = useState(0);
 

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.get(
        `http://localhost:8080/api/auth/audience/viewAllPosts`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setPost(response.data.payload);
      setP(response.data.payload);
      console.log(response.data.payload);

      const customerResponse = await axios.get(
        "http://localhost:8080/api/auth/user/profile",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setCustomer(customerResponse.data.payload);
      if (response && customerResponse) setIsLoading(true);
    };
    fetchData();
  }, [isLike]);

  const handleFollow = async (p) => {
    const response = await axios.post(
      `http://localhost:8080/api/auth/follow?creatorId=${p.creatorId}`,null,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    console.log(response);
  };
  console.log(post);

  const handleLike = async (p) => {
    const response = await axios.post(
      `http://localhost:8080/api/auth/interaction/like?postId=${p.postId}`,
      null,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (response.status === 200) {
      setIsLike(!isLike)
    }
  };

  const isLiked = async (p) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/auth/interaction/view?postId=${p.postId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      console.log(response.data.payload);
      if (response.data.payload === null) {
        return false;
      } else {
        console.log(response.data.payload.isLiked)
        return response.data.payload.isLiked;
      }
    } catch (error) {
      console.error('Error fetching:', error);
      return false;
    }
  };

  return (
    <>
      <Spin spinning={!isLoading} fullscreen />
      <div className="flex flex-col justify-center items-center w-full">
        {post?.length > 0
          ? post?.map((p) => (
            <Card
              key={p.postId}
              className="justify-center flex bg-white shadow-md shadow-gray-300 rounded-md mb-5 w-1/2"
            >
              <div className="flex justify-between gap-3">
                <Link to={customer.userId !== p.creatorId ? `/artistProfile?creatorId=${p.creatorId}` : "/viewEwallet"}>
                  <Avatar rounded>
                    <div className="space-y-1 dark:text-white">
                      <div className="font-medium">{p.creatorName}</div>
                      <div className="text-sm text-gray-500 dark:text-gray-400">{p.publishDate}</div>
                    </div>
                  </Avatar>
                </Link>
                {customer.userId !== p.creatorId ? <div
                  onClick={() => handleFollow(p)}
                  className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1"
                >
                  <AiOutlineUserAdd
                    size={20}
                    style={{ color: "#fff", fontWeight: "bold" }}
                  />
                  <button type="submit">Follow</button>
                </div> : <div><Link to={`/updatePost?postId=${p.postId}`}>Update Post</Link></div>}
              </div>


              <div>
                <p className="text-sm my-2">{p.description}</p>
                <div className="w-full h-screen max-h-[50vh]">
                  <Carousel pauseOnHover className="w-full mx-auto">
                    {p.artList.map((item, index) => (
                      <div key={index}>
                        <Link to={`/singlePost?postId=${p.postId}`}>
                          <img
                            src={item.imagePath}
                            className="rounded-md w-[700px] mx-auto"
                            alt={`Post Image - ${p.description}`}
                          />
                        </Link>
                      </div>
                    ))}
                  </Carousel>
                </div>
              </div>

              <div className="mt-1 flex justify-between">
                <div className="flex justify-between gap-16">
                  <button className="flex gap-2 items-center" onClick={() => handleLike(p)}>
                    {isLiked(p) === true ? (
                      <FaHeart
                        size={20}
                        style={{ color: "red", fontWeight: "bold" }}
                      />
                    ) : (
                      <FaRegHeart
                        size={20}
                        style={{ color: "#000", fontWeight: "bold" }}
                      />
                    )}
                    {p.numberOfLikes}
                  </button>

                  <button className="flex gap-2 items-center">
                    <FaRegCommentDots
                      size={20}
                      style={{ color: "#000", fontWeight: "bold" }}
                    />
                    {/* {numComments} */}
                  </button>
                </div>
              </div>
              <InputComment postId={p.postId}/>
            </Card>
          ))
          : null}
      </div>
    </>
  );
};

export default PostCard;
