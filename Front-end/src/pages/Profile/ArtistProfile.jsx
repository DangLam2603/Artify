import axios from "axios";
import React, { useEffect, useState } from 'react';
import CardProfile from '../../components/CardProfile';
import FooterPart from '../../components/FooterPart';
import ImageList from '../../components/ImageList';
import NavBar from '../../components/NavBar';
import PostCard from '../../components/PostCard';
import RequestArt from '../../components/RequestArt';
import FollowList from '../../components/FollowList';


const ArtistProfile = () => {
  const [activeComponent, setActiveComponent] = useState('post');
  const [post, setPost] = useState([]);
  const [follow, setFollow] = useState([]);

  const params = new URLSearchParams(window.location.search);
  const creatorId = params.get("creatorId");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.get(
        `http://localhost:8080/api/auth/getPostByCreator?creatorId=${creatorId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setPost(response.data.payload);

      const followResponse = await axios.get(
        `http://localhost:8080/api/auth/get_all_follower?creatorId=${creatorId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setFollow(followResponse.data.payload);
    }
    fetchData();
  }, []);

  console.log(post)
  console.log(follow)


  const handlePostButtonClick = () => {
    setActiveComponent('post');
  };

  const handleGalleryButtonClick = () => {
    setActiveComponent('gallery');
  };

  const handleFollowerButtonClick = () => {
    setActiveComponent('follower');
  };

  const handleRequestButtonClick = () => {
    setActiveComponent('request');
  };

  const renderActiveComponent = () => {
    if (activeComponent === 'post') {
      return <PostCard />;
    } else if (activeComponent === 'gallery') {
      return <ImageList />;
    } else if (activeComponent === 'follower') {
      return <FollowList follow={follow} />;
    } else if (activeComponent === 'request') {
      return <RequestArt creatorId={creatorId} />;
    }
  };

  console.log(activeComponent)

  return (
    <div className='w-full h-screen bg-gray-100'>
      <NavBar />

      <CardProfile
        onPostButtonClick={handlePostButtonClick}
        onGalleryButtonClick={handleGalleryButtonClick}
        onFollowerButtonClick={handleFollowerButtonClick}
        onRequestButtonClick={handleRequestButtonClick}
        p={post}
        creatorId={creatorId}
        follow={follow}
      />

      <div className='w-full mt-72 bg-gray-100'>
        {renderActiveComponent()}
      </div>
      <FooterPart />
    </div>

  )
}

export default ArtistProfile