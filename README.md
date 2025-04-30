==>Usecase: Build proof of concept app ( not production ready ) with features: 1) search movies 2) if the query is empty show Top Rated movies 3) locally maintained movie favorite status 4) movie details

==>Techstack: CLEAN, MVVM, KOTLIN, COROUTINES/FLOW, COMPOSE, NAVIGATION, KOIN, ROOM, KTOR...

==>Setup:
Favorite status is maintained as an entry in FavoritesTable(movieId) in Db.
==>MOVIES LIST SCREEN: movies are cached in the database, screen listen to MoviesDb INNER JOIN MoviesIndexDb LEFT JOIN FavouritesIndexDb. Since its list of movies merging of the movies from api and locally maintained favorite status is done in DATA layer.
==>DETAILS SCREEN: details are not cached, screen fetch movie details and listen for Favourite status of movie with id from Db. Merging of the details with the favourite status is done in the PRESENTATION layer.
==>FAVORITES SCREEN: listen to MoviesDb INNER JOIN FavoritesIndexDb

Repository is the entry point to the Domain layer, haven't add UseCase-s since it would only act as a connection between Ui and Repo since there's not much of a business logic.

