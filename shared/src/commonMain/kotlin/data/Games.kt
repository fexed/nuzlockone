package data

import language

class Game(
    var id: Int = -1,
    var index: Int = -1,
    var title: String = "",
    var imageUrl: String = "",
    var isValid: Boolean = false
)

fun getGameImageUrl(id: Int): String {
    return when (id) {
        1 -> "https://pm1.aminoapps.com/5815/503573684e4ddc3647dd3c8bd6f0de734297d902_00.jpg" //RED
        2 -> "https://upload.wikimedia.org/wikipedia/en/thumb/a/aa/Pok%C3%A9mon_Red%2C_Blue%2C_and_Yellow_screenshot.png/220px-Pok%C3%A9mon_Red%2C_Blue%2C_and_Yellow_screenshot.png" ///BLUE
        3 -> "https://upload.wikimedia.org/wikipedia/it/8/82/Pokemon_gb_ashandpikachu.png" //YELLOW
        4 -> "https://realotakugamer.com/wp-content/uploads/2017/10/500full-pokemon-gold-version-screenshot.gif" //GOLD
        5 -> "https://www.vgchartz.com/games/pics/2223263aaa.jpg" //SILVER
        6 -> "https://i0.wp.com/pokemonfourever.com/wp-content/uploads/2020/03/pokemon-crystal-screenshot-4_jpg.jpg?ssl=1" //CRYSTAL
        7 -> "https://media.moddb.com/images/games/1/23/22578/pokemon-ruby-screenshot-3.jpg" //RUBY
        8 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRds-QIeI1M4JLfR-mGgE6TFZ4I1-NZ_YASFN_c6tQmVPHIWVFkuw_3pLdcqLlip4eMuMs&usqp=CAU" //SAPPHIRE
        9 -> "https://www.giantbomb.com/a/uploads/original/0/5141/252030-pokemon_emerald_2.jpg" //EMERALD
        10 -> "https://media.rawg.io/media/resize/420/-/screenshots/256/2568bd7091069360362b9d6e23f73e8d.jpg" //FIRE RED
        11 -> "https://www.rpgfan.com/wp-content/uploads/2020/10/Pokemon-FireRed-LeafGreen-Screenshot-041.jpg" //LEAF GREEN
        12 -> "https://pbs.twimg.com/ext_tw_video_thumb/1376552587625963524/pu/img/8YtAGnMEzHJyemDB.jpg" //DIAMOND
        13 -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUXxBqky5GidgUWHnGeyMuogO0mdpjx1NVkQkWG3LkF4l81tUbu30OmJ4He21tInpARBA&usqp=CAU" //PEARL
        14 -> "https://images.nintendolife.com/screenshots/15790/900x.jpg" //PLATINUM
        15 -> "https://www.rpgfan.com/wp-content/uploads/2020/10/Pokemon-HeartGold-SoulSilver-Screenshot-008.jpg" //HEART GOLD
        16 -> "https://cdn.thegamesdb.net/images/original/screenshots/2369-1.jpg" //SOUL SILVER
        17 -> "https://images.nintendolife.com/screenshots/30554/900x.jpg" //BLACK
        18 -> "https://images.nintendolife.com/screenshots/29973/900x.jpg" //WHITE
        19 -> "https://cdn.staticneo.com/p/2004/1/pokemon_colosseum_import_image5.jpg" //COLOSSEUM
        20 -> "https://i.ytimg.com/vi/FnB8uoZPJ4A/maxresdefault.jpg" //XD
        21 -> "https://www.serebii.net/black2white2/hiougi3.png" //BLACK2
        22 -> "https://www.gamerstemple.net/vg/games74/007608/007608s005.jpg" //WHITE2
        23 -> "https://cdn.videogamesblogger.com/wp-content/uploads/2013/01/pokemon-x-and-y-screenshots.jpg" //X
        24 -> "https://cdn.videogamesblogger.com/wp-content/uploads/2013/01/pokemon-x-and-y-boy-trainer-screenshot-640x384.jpg" //Y
        25 -> "https://images.nintendolife.com/screenshots/63580/large.jpg" //OMEGA RUBY
        26 -> "https://images.nintendolife.com/screenshots/63577/900x.jpg" //ALPHA SAPPHIRE
        27 -> "https://poketouch.files.wordpress.com/2016/11/pokemon_sun_and_moon_screenshot.png" //SUN
        28 -> "https://www.rpgfan.com/wp-content/uploads/2020/10/Pokemon-Sun-Moon-Screenshot-098.jpg" //MOON
        29 -> "https://nintendoeverything.com/wp-content/uploads/sites/1/nggallery/ultrasunandmoon/111.jpg" //ULTRA SUN
        30 -> "https://nintendoeverything.com/wp-content/uploads/sites/1/nggallery/ultrasunandmoon/110.jpg" //ULTRA MOON
        31 -> "https://pbs.twimg.com/media/DeaJfiEVMAAUCbe.jpg" //LETS GO PIKACHU
        32 -> "https://www.famitsu.com/images/000/166/427/5bd0719531df3.jpg" //LETS GO EEVEE
        33 -> "https://www.newgamenetwork.com/images/uploads/gallery/PokemonShield/pkshield_02.jpg" //SWORD
        34 -> "https://nintendoeverything.com/wp-content/uploads/sites/1/nggallery/pokemon-sword-shield-22720/In-Game-Screenshot-7.jpg" //SHIELD
        35 -> "" //ISLE ARMOR
        36 -> "" //CROWN TUNDRA
        37 -> "https://sm.ign.com/ign_ap/gallery/p/pokemon-br/pokemon-brilliant-diamond-and-shining-pearl-screenshot-galle_1uah.jpg" //BRILLIANT DIAMOND
        38 -> "https://www.gematsu.com/wp-content/uploads/2021/10/Pokemon-Brilliant-Diamond-and-Shining-Pearl_2021_10-13-21_011.jpg" //SHINING PEARL
        39 -> "https://press-start.com.au/wp-content/uploads/2022/01/Pokemon-Legends-Arceus.jpg" //LEGENDS ARCEUS
        40 -> "https://mysterio.yahoo.com/api/res/1.2/.ILa1OjtuRvDxG1N2fdG4A--/ZHByPTI7dz04NzU7YXBwaWQ9ZW5nYWRnZXQ-/https://s.yimg.com/os/creatr-uploaded-images/2022-10/b3797ea0-50b5-11ed-b3db-3f5ad5a02e3d.cf.webp" //SCARLET
        41 -> "https://www.gameinformer.com/sites/default/files/styles/body_default/public/2022/08/03/9d77ecea/pokemon_sv_screenshot_6.jpg" //VIOLET
        42 -> "" //TEAL MASK
        43 -> "" //INDIGO DISK
        else -> ""
    }
}

fun gameNameFix(id: Int, title: String): String {
    return if (language == "it") {
        when (id) {
            33 -> "Spada"
            34 -> "Scudo"
            else -> title
        }
    } else title
}