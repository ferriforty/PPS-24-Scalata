# [0.3.0](https://github.com/ferriforty/PPS-24-Scalata/compare/v0.2.0...v0.3.0) (2025-07-08)


### Bug Fixes

* **enemy:** Movement fix, now works perfectly ([d2d5bbf](https://github.com/ferriforty/PPS-24-Scalata/commit/d2d5bbfb5c4859b1b985f4bf32510f4fa4047aec))
* **test:** fix in test to work with new EnemyMovementUseCase ([e5f7831](https://github.com/ferriforty/PPS-24-Scalata/commit/e5f78319789fd3de1bc28a2fafb675ec83244851))
* **test:** fix test on gameError change ([72f20d5](https://github.com/ferriforty/PPS-24-Scalata/commit/72f20d5c8088ce0aafc0351a4c2774a9a3307570))
* **test:** small fix in infinite loop test in gamecontroller ([6d06b3e](https://github.com/ferriforty/PPS-24-Scalata/commit/6d06b3ecfa416e4cab45ee7ae9a05a801f3a0a30))
* **view:** Fix bug, stdline not working in bash ([cf2a81e](https://github.com/ferriforty/PPS-24-Scalata/commit/cf2a81e5c1182c002254854f81149eea14d5ac63))


### Features

* **enemy:** Add Scala2P and init EnemyMovementUseCase ([03a13c3](https://github.com/ferriforty/PPS-24-Scalata/commit/03a13c32029dc01904418f31991993fc7b830870))
* **enemy:** Init EnemyMovementUseCase, ready to be implemented in prolog ([e9fcd84](https://github.com/ferriforty/PPS-24-Scalata/commit/e9fcd84e192e3a59553a77b21a53b1c2a590ae31))
* **enemy:** Movement enemy done in prolog ([c0902f9](https://github.com/ferriforty/PPS-24-Scalata/commit/c0902f9dd5ccd7b43a2c44771d10c3b0ae0cb40f))
* **entity:** Add and implement Item trait and Weapon, Add List of enemies and items in room ([9ed6546](https://github.com/ferriforty/PPS-24-Scalata/commit/9ed6546b1e9311cca483b322a3f814e78a027ddb))
* **entity:** Add Components for items ([efbc129](https://github.com/ferriforty/PPS-24-Scalata/commit/efbc1292c229ebfb3e65506d7c7cff87ab799157))
* **entity:** Add Door and sign items ([c10ed83](https://github.com/ferriforty/PPS-24-Scalata/commit/c10ed833d654f4b335a92cbf5e5b23254aa79341))
* **entity:** Add Enemy factory and create inteface for factories ([31b3147](https://github.com/ferriforty/PPS-24-Scalata/commit/31b314795dbf3bbb70224a02d4e515f61fe9bc64))
* **entity:** Define Enemy entity and add Combact component ([1065084](https://github.com/ferriforty/PPS-24-Scalata/commit/1065084161ff8a746cc73b57a0028a48117805fa))
* **entity:** Enemy Attack Use Case implement ([49f5efc](https://github.com/ferriforty/PPS-24-Scalata/commit/49f5efc6f346171371616bca9290ab107781d139))
* **entity:** Implement interact use case and refactor GameError ([d21acfa](https://github.com/ferriforty/PPS-24-Scalata/commit/d21acfae4034cdba681aad65643ec67630234b46))
* **player:** Add F-Bounded polymorphism to components ([282e9e6](https://github.com/ferriforty/PPS-24-Scalata/commit/282e9e6f10eda59de1feaaf95774d54fe59616d4))
* **player:** Add player commands and adapt GameRunning controller ([cfb1dda](https://github.com/ferriforty/PPS-24-Scalata/commit/cfb1dda92918c5c35906ce912a4fcb152b9100dc))
* **player:** Add Usable given component and implement it for usable items ([973da68](https://github.com/ferriforty/PPS-24-Scalata/commit/973da6857f78bd09dbeb9aefbc3343c0b1790fca))
* **player:** Define Player UseCases ([5aa23dd](https://github.com/ferriforty/PPS-24-Scalata/commit/5aa23dda4079759e8c5b0b8e3183c48087e01e18))
* **player:** Implement Alive component for player and test it ([79b0671](https://github.com/ferriforty/PPS-24-Scalata/commit/79b0671b4743e16b25ff5067ec20634117fdad05))
* **player:** Implement new level generation when interacting with exitdoor ([737fc30](https://github.com/ferriforty/PPS-24-Scalata/commit/737fc3090bdb3e8ec1bf689df799ea26d614c19b))
* **player:** Implement player movement use case ([606d5f9](https://github.com/ferriforty/PPS-24-Scalata/commit/606d5f93ddd44c8125da2f71201cb93fe531a6b5))
* **view:** Add gamestate view ([f0f69d7](https://github.com/ferriforty/PPS-24-Scalata/commit/f0f69d7aac2d8a5315362851fe1437e577245caa))
* **view:** Add View for displaying enemies and items ([90667cb](https://github.com/ferriforty/PPS-24-Scalata/commit/90667cbdcf00496b3bafbd4dd4e405ad103369e4))
* **world:** Add Door and sign creation to FloorGeneration ([8e67f77](https://github.com/ferriforty/PPS-24-Scalata/commit/8e67f771bc251661f6fbe02f3ab0bc409f51ad74))
* **world:** Add generation of enemies in floorgeneration ([95bad4e](https://github.com/ferriforty/PPS-24-Scalata/commit/95bad4e5d81f73b3f903382173a55aba982fc7c3))
* **world:** Add ItemFactory adn dust and potion items ([45f0466](https://github.com/ferriforty/PPS-24-Scalata/commit/45f0466dafbc64ce10d4be4f77dc24a4bfceeaa0))
* **world:** Implement generation of items and enemies in the FloorGenerator ([6082488](https://github.com/ferriforty/PPS-24-Scalata/commit/6082488911e25518f7a6ef950521e28a3548f962))

# [0.2.0](https://github.com/ferriforty/PPS-24-Scalata/compare/v0.1.1...v0.2.0) (2025-06-29)


### Features

* **gameloop:** Init GameRunning use case for world creation ([63db20d](https://github.com/ferriforty/PPS-24-Scalata/commit/63db20d8de9a4c9505cbf4997f4632ef9206c630))
* **world:** Add Constants for the world ([3a1823d](https://github.com/ferriforty/PPS-24-Scalata/commit/3a1823df4ae2a46f042e16d89648d9b2527d0dbe))
* **world:** Add Random Rooms names ([ea732d5](https://github.com/ferriforty/PPS-24-Scalata/commit/ea732d50bbf88296b4ce74ccd1c26ef377b66b83))
* **world:** Create rooms arrangement for the floor generation ([629f65d](https://github.com/ferriforty/PPS-24-Scalata/commit/629f65de81aaf5b85f4ad4d73608271d25dc0339))
* **world:** Impl floor generation entirely ([7e2380e](https://github.com/ferriforty/PPS-24-Scalata/commit/7e2380ea927a8b0d4cba16e92f349bd11725966c))
* **world:** Implement Room utilies and display world ([2a4a822](https://github.com/ferriforty/PPS-24-Scalata/commit/2a4a82281f260cce23d9f90c2f501fdc322faf9d))

## [0.1.1](https://github.com/ferriforty/PPS-24-Scalata/compare/v0.1.0...v0.1.1) (2025-06-25)


### Bug Fixes

* Changelog error in tag ([687de13](https://github.com/ferriforty/PPS-24-Scalata/commit/687de1372d045c3f0b2a0c4dc65d69d68e6b4676))

# 0.1.0 (2025-06-25)


### Bug Fixes

* merge conflict ([3370fa8](https://github.com/ferriforty/PPS-24-Scalata/commit/3370fa843468227c23695e9adf60afe12e3d63e0))
* **merge:** Merge conflict fix ([1a5e72a](https://github.com/ferriforty/PPS-24-Scalata/commit/1a5e72ae42400eabd06f366d13f0375b8d327952))


### Features

* **gameloop:** Add Game over case ([031b43a](https://github.com/ferriforty/PPS-24-Scalata/commit/031b43a13020f3484d8f2992ecc10f07f15eb451))
* **gameloop:** Add util package with gamestate enum and direction enum ([53a8801](https://github.com/ferriforty/PPS-24-Scalata/commit/53a88012d4ea428b17d6700f942153138c3f7d49))
* **gameloop:** Add worldGenerator to the game loop ([900099d](https://github.com/ferriforty/PPS-24-Scalata/commit/900099d9aff02d2bfc175db2abfd4f6df48a97b4))
* **gameloop:** Controller Champ Select impl ([bfaef3f](https://github.com/ferriforty/PPS-24-Scalata/commit/bfaef3feca163134fcbfecfd3b7317f7e0cbdb02))
* **gameloop:** gameEngine first impl and Controllers creation ([a30e6ce](https://github.com/ferriforty/PPS-24-Scalata/commit/a30e6ceeec67143490b0cdbad433c3770fe4ee55))
* **player:** Add components for player and create player entity ([322c57f](https://github.com/ferriforty/PPS-24-Scalata/commit/322c57fccb63dcb77a0d65509795c533c65f0888))
* **player:** Add player factory ([4d958c8](https://github.com/ferriforty/PPS-24-Scalata/commit/4d958c873d8d8bee3e0402151a915a16467880f8))
* **view:** Add MenuView ([a654172](https://github.com/ferriforty/PPS-24-Scalata/commit/a65417238df19b1d0881ea9c7a53b97e3ce496ce))
* **world:** Add world builder and level generator first behaviour ([8a3672a](https://github.com/ferriforty/PPS-24-Scalata/commit/8a3672a5d407d3da5aa12bbe46f58ddebe2de9bd))
* **world:** Create Point2D to manage spatialPosition and first world and room class ([66d6011](https://github.com/ferriforty/PPS-24-Scalata/commit/66d6011ba1d399a2d4c5c6bfb49b4c568453199d))
* **world:** Impl few utility methods for world and room ([643aeeb](https://github.com/ferriforty/PPS-24-Scalata/commit/643aeebf852fc1bce9691f5bf31921020b40dcf8))
