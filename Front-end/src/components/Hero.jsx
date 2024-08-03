import React from 'react'
import { Carousel } from 'flowbite-react';
import wallpaper_1 from "../assets/wallpaper_1.jpg"
import wallpaper_2 from "../assets/wallpaper_2.jpg"
import wallpaper_3 from "../assets/wallpaper_3.jpg"

const Hero = () => {
  return (
    <div>
      <div className='min-h-screen h-screen mx-auto max-w-screen-xl p-4 mt-8'>
        <Carousel className='w-full mx-auto'>
          <div className="relative flex flex-col md:flex-row-reverse items-center justify-between my-20 md:my-8 py-12">
            <div className='h-full w-full'>
              <img src={wallpaper_1} className="h-full w-full object-cover" alt="Wallpaper" />
              <div className='absolute inset-0 bg-black opacity-75'></div> 
            </div>
            <div className='absolute w-full items-center px-40'>
              <h1 className='text-7xl font-bold mb-6 text-white'>Enjoy Artwork</h1>
              <p className="text-white text-2xl">Shop For Art You Love</p>
            </div>
          </div>
          <div className="relative flex flex-col md:flex-row-reverse items-center justify-between my-20 md:my-8 py-12">
            <div className='h-full w-full'>
              <img src={wallpaper_2} className="h-full w-full object-cover" alt="Wallpaper" />
              <div className='absolute inset-0 bg-black opacity-75'></div> 
            </div>
            <div className='absolute w-full items-center px-40'>
              <h1 className='text-7xl font-bold mb-6 text-white'>Discover Art You Love</h1>
              <p className="text-white text-2xl">Browse Curated Collections Updated Daily.</p>
            </div>
          </div>
          <div className="relative flex flex-col md:flex-row-reverse items-center justify-between my-20 md:my-8 py-12">
            <div className='h-full w-full'>
              <img src={wallpaper_3} className="h-full w-full object-cover" alt="Wallpaper" />
              <div className='absolute inset-0 bg-black opacity-75'></div> 
            </div>
            <div className='absolute w-full items-center px-40'>
              <h1 className='text-7xl font-bold mb-6 text-white'>Trending Art Picture</h1>
              <p className="text-white text-2xl">Browse Curated Collections Updated Daily.</p>
            </div>
          </div>
        </Carousel>
      </div>
    </div>
  )
}

export default Hero