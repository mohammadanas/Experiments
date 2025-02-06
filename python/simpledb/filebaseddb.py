from typing import Optional, Dict

class DB:
	def __init__(self):
		self.filename = 'db.csv'
		self.delimiter = ','
		self.index: Dict[str, int] = {}

	def insert(self, key: str, value: str) -> None:
		with open(self.filename, 'a') as f:
			self.index[key] = f.tell()
			print(f'{key}{self.delimiter}{value}', file=f)


	def get(self, key: str) -> Optional[str]:
		with open(self.filename, 'r') as file:
			if key in self.index:
				file.seek(self.index[key])
				return ''.join(file.readline().split(self.delimiter)[1:])[:-1]
			else:
				return None

if __name__ == '__main__':
	db = DB()

	db.insert('example@gmail.com', '{"name": "anas", "age": 30}')
	db.insert('test@gmail.com', '{"name": "sadiya", "age": 25}')
	print(db.get('example@gmail.com'))
	print(db.get('test@gmail.com'))
