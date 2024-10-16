import pandas as pd
from sklearn.preprocessing import LabelEncoder

# Your dummy data (replace with your actual data)
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'],
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'],
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Initialize a single LabelEncoder
le = LabelEncoder()

# Stack all columns into a single Series, then fit the LabelEncoder
le.fit(df.stack().unique()) 

# Apply the LabelEncoder to each column and assign the encoded values to a new DataFrame
df_encoded = df.apply(le.transform) 

# Display the encoded DataFrame
print(df_encoded)