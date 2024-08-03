import React, { useState, useEffect } from "react";
import { Avatar, Card } from "flowbite-react";
import { AiOutlineUserAdd } from "react-icons/ai";
import { Spin } from "antd";

const CardProfile = ({
  onPostButtonClick,
  onGalleryButtonClick,
  onFollowerButtonClick,
  onRequestButtonClick,
  p,
  creatorId,
  follow,
}) => {
  const [customer, setCustomer] = useState({});

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
  }, []);
  const [isLoading, setIsLoading] = useState(false);

  const stats = [
    { id: 1, name: "Posts", value: p.length },
    { id: 2, name: "Photos", value: calculatePhotos(p) },
    { id: 3, name: "Followers", value: follow.length },
  ];

  function calculatePhotos(posts) {
    let photoCount = 0;
    for (const post of posts) {
      photoCount += post.artList.length;
    }
    return photoCount;
  }

  return (
    <>
      <div className="h-[40%] ">
        {p != null || p != undefined ? (
          <div className="relative w-full h-full bg-[#2f6a81]">
            <div className="absolute flex flex-col items-center justify-center w-full top-1/2">
              <Card className="w-[40%]">
                <div className="flex flex-col items-center">
                  <Avatar rounded size="lg" />
                  <h5 className="mb-1 text-xl font-medium text-gray-900 dark:text-white">
                    {p[0]?.creatorName}
                  </h5>
                  <span className="text-sm text-gray-500 dark:text-gray-400">
                    Artist
                  </span>
                  <div className="mt-4 flex space-x-3 lg:mt-6">
                    {customer.userId === p[0]?.creatorId ? (
                      <>Update Post</>
                    ) : (
                      <div className="cursor-pointer font-semibold border-2 sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full  hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2">
                        <AiOutlineUserAdd
                          size={20}
                          className="hover:text-[#2f6a81]"
                        />
                        <button type="submit">Follow</button>
                      </div>
                    )}

                    <div className="cursor-pointer font-semibold border-2 sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full  hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2">
                      <AiOutlineUserAdd
                        size={20}
                        className="hover:text-[#2f6a81]"
                      />
                      <button type="button" onClick={onRequestButtonClick}>
                        Request
                      </button>
                    </div>
                  </div>

                  <div className="mt-4 flex lg:mt-6">
                    <div className="mx-auto max-w-7xl px-6 lg:px-8">
                      <dl className="grid grid-cols-1 gap-x-14 gap-y-16 text-center lg:grid-cols-3">
                        {stats.map((stat) => (
                          <div
                            key={stat.id}
                            className="mx-auto flex max-w-xs flex-col gap-y-1"
                          >
                            <dt className="font-normal text-gray-500 leading-7 uppercase">
                              {stat.name}
                            </dt>
                            <dd className="order-first text-xl font-semibold tracking-tight sm:text-2xl">
                              {stat.value}
                            </dd>
                          </div>
                        ))}
                      </dl>
                    </div>
                  </div>
                </div>
              </Card>

              <div className="flex justify-center mt-5">
                <div class="inline-flex rounded-md shadow-sm" role="group">
                  <button
                    onClick={onPostButtonClick}
                    type="button"
                    className=" px-4 py-2 text-sm font-medium text-gray-900 bg-transparent border border-gray-300 rounded-s-lg hover:bg-[#2f6a81] hover:text-white focus:z-10 focus:ring-2 focus:ring-[#2f6a81]-500 focus:bg-[#2f6a81] focus:text-white dark:border-white dark:text-white dark:hover:text-white"
                  >
                    Post
                  </button>
                  <button
                    onClick={onGalleryButtonClick}
                    type="button"
                    class="px-4 py-2 text-sm font-medium text-gray-900 bg-transparent border-t border-b border-gray-300 hover:bg-[#2f6a81] hover:text-white focus:z-10 focus:ring-2 focus:ring-[#2f6a81]-500 focus:bg-[#2f6a81] focus:text-white dark:border-white dark:text-white dark:hover:text-white "
                  >
                    Gallery
                  </button>
                  <button
                    onClick={onFollowerButtonClick}
                    type="button"
                    class="px-4 py-2 text-sm font-medium text-gray-900 bg-transparent border border-gray-300 rounded-e-lg hover:bg-[#2f6a81] hover:text-white focus:z-10 focus:ring-2 focus:ring-[#2f6a81]-500 focus:bg-[#2f6a81] focus:text-white dark:border-white dark:text-white dark:hover:text-white "
                  >
                    Follower
                  </button>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <Spin spinning={!isLoading} fullscreen />
        )}
      </div>
    </>
  );
};

export default CardProfile;
