# Spring5DiceGame

El proyecto se trata de una aplicación que utiliza persistencia de datos con una base de datos MySQL. La aplicación permite a los usuarios registrarse, autenticarse y realizar operaciones relacionadas tanto con los jugadores (crear,  actualizar), y con el juego (tirar dados, listar, borrar jugadas).

La aplicación consta de varias clases principales:

		PlayerServiceImpl:Es la implementación  de la interface PlayerService, contienen la lógica para las operaciones relacionadas con los jugadores. Esta clase se encarga de manejar las operaciones de creación, actualización, eliminación y consulta de jugadores y juegos.
		
		PlayerController: Es un controlador de Spring MVC que define las API REST para interactuar con los jugadores y juegos. Proporciona métodos para crear jugadores, actualizar sus nombres, obtener juegos de un jugador, obtener todos los jugadores, obtener estadísticas de los jugadores, etc.
		
		AuthenticationController: Es otro controlador que maneja la autenticación y registro de usuarios. Proporciona métodos para registrar un nuevo usuario y autenticar a un usuario existente.
		
		AuthenticationService: Es un servicio que se encarga de la autenticación y generación de tokens JWT para los usuarios.
		
		JwtAuthenticationFilter: Es un filtro de Spring Security que verifica y procesa los tokens JWT en las solicitudes entrantes. Si el token es válido, establece la autenticación en el contexto de seguridad de Spring.
		
		JwtService: Es un servicio que proporciona métodos para generar y verificar tokens JWT. Utiliza una clave secreta para firmar y verificar los tokens.
		
		DataInitializer: Es una clase que se ejecuta al inicio de la aplicación y se encarga de crear un usuario administrador inicial si no existe.
		
		SecurityConfig y ApplicationConfig: Son clases de configuración de Spring Security que establecen la configuración de seguridad para la aplicación. Configuran la autenticación, autorización y otros aspectos de la seguridad.

Al iniciar la aplicación, se ejecuta la clase DataInitializer. Esta clase comprueba si existe un usuario administrador en la base de datos. Si no existe, crea un usuario administrador predeterminado con los siguientes detalles: nombre "Admin", email "admin@admin.com" y contraseña "12345678". El usuario administrador se crea con el rol de administrador.
Los usuarios normales pueden registrarse en la aplicación a través de la API REST expuesta por AuthenticationController. Al registrarse, proporcionan un nombre, email y contraseña. El registro crea un nuevo usuario en la base de datos con el rol de usuario normal. También se crea automáticamente un jugador asociado al usuario, utilizando el mismo nombre proporcionado durante el registro.
		
Los usuarios pueden autenticarse utilizando su email y contraseña a través de la API REST expuesta por AuthenticationController. Si la autenticación es exitosa, se genera un token JWT (JSON Web Token) para el usuario. Este token se devuelve al cliente y se debe incluir en las solicitudes posteriores como encabezado de autorización para acceder a los recursos protegidos.
El controlador PlayerController proporciona varios endpoints para realizar operaciones relacionadas con los jugadores y juegos. Sin embargo, ciertos métodos están restringidos según el rol del usuario autenticado:
		
	      Los usuarios normales solo pueden acceder a los métodos que obtienen información sobre su propio jugador y realizan operaciones relacionadas con sus propios juegos.
	      El usuario administrador tiene acceso a todos los métodos, incluidos los que obtienen información de todos los jugadores, realizan operaciones en los juegos de cualquier jugador y obtienen estadísticas generales de los jugadores.
		
El filtro JwtAuthenticationFilter se encarga de verificar el token JWT en cada solicitud entrante y establecer la autenticación en el contexto de seguridad de Spring. Si el token es válido y corresponde a un usuario autenticado, se permitirá el acceso a los recursos protegidos según los roles y permisos asignados al usuario.

Ejemplos:
		
		body registro User:
			
				{
 			 "email": “ejemplo@ejemplo.com,
 			 "firstname": “nombre,
  			"password": "contraseña"
			}

		body autorizacion User y Admin:
	
					{
 			 "email": “e”jemplo@ejemplo.com,
  			"password": "contraseña"
			}

		body creación jugador por parte de admin ya que el user al registrarse crea su propio Player:

				{
				“name”:”ejemplo”,
				}
