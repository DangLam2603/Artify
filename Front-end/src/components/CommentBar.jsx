import React, { useEffect, useState } from "react";
import SinglePostComment from "./SinglePostComment";
import login from "../assets/login.jpg";
import InputComment from "./InputComment";
import axios from "axios";

const CommentBar = ({ postId }) => {
  const token = localStorage.getItem("token");
  const [commentList, setCommentList] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      const comments = axios.get(
        `http://localhost:8080/api/auth/comment/view?postId=${postId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (comments) {
        setCommentList((await comments).data.payload);
      }
    };
    fetchData();
  }, []);


  const numComments = commentList.length;

  return (
    <div className="flex flex-col items-start justify-between w-full h-full ">
      <div className="w-full flex flex-col items-start gap-10 mb-2">
        <h1 className="text-lg font-semibold text-[#2f6a81]">Comments ({numComments})</h1>
        <div
          className="flex flex-col w-full gap-8 overflow-y-scroll pr-8"
          style={{ maxHeight: "300px" }}
        >
          {commentList.length > 0 &&
            commentList.map((item, index) => (
              <SinglePostComment
                key={index}
                billyGreenOne={login}
                timezone={item.comment}
                name={item.userName}
                className="flex flex-row justify-center w-full"
              />
            ))}
        </div>
      </div>
      <div className="flex flex-col justify-end w-full">
        <InputComment postId={postId} />
      </div>
    </div>
  );
};

export default CommentBar;
