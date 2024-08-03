import React from 'react'
import NavBar from '../../components/NavBar';
import FooterPart from '../../components/FooterPart'
import CardArtist from '../../components/CardArtist';

const ArtistList = () => {
    return (
        <div>
            <NavBar />
            <div className='h-full py-28 mt-10 bg-gray-100'>
                <CardArtist />
            </div>
            <FooterPart />
        </div>
    )
}

export default ArtistList