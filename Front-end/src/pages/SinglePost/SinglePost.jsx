import React from 'react'
import CardItem from '../../components/CardItem';
import NavBar from '../../components/NavBar'
import FooterPart from '../../components/FooterPart'

const SinglePost = () => {
    return (
        <div className='h-screen'>
            <NavBar />
            <div className="flex flex-row justify-center w-full h-screen bg-gray-100">
                <div className="flex flex-row justify-center w-full pt-28 ">
                    <CardItem />
                </div>
            </div>
            <FooterPart />
        </div>
    )
}

export default SinglePost