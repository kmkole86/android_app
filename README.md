==>Usecase: Build proof of concept app ( not production ready ) with features: 1) search movies 2) if query is empty show Top Rated movies 3) locally maintained movie favourite status 4) movie details

==>Techstack: CLEAN, MVVM, KOTLIN, COROUTINES/FLOW, COMPOSE, NAVIGATION, KOIN, ROOM, KTOR...

==>Setup:
Favourite status is maintained as entry in FavouritesTable(movieId) in Db.
==>MOVIES LIST SCREEN: movies are cached in the database, screen listen to MoviesDb INNER JOIN MoviesIndexDb LEFT JOIN FavouritesIndexDb. Since its list of movies merging of the movies from api and locally mainained favourite status is done i DATA layer.
==>DETAILS SCREEN: details are not cached, screen fetch movie details and listen for Favourite status of movie with id from Db. Merging of the details with the favourite status id done in PRESENTATION layer.
==>FAVOURITES SCREEN: listen to MoviesDb INNER JOIN FavouritesIndexDb

Repository is the entry point to the Domain layer, havent add UseCase-s since it would only act as a connection between Ui and Repo since theres not much of a business logic.

