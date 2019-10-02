import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IDiscordUserProfile, defaultValue } from 'app/shared/model/bot/discord-user-profile.model';

export const ACTION_TYPES = {
  FETCH_DISCORDUSERPROFILE_LIST: 'discordUserProfile/FETCH_DISCORDUSERPROFILE_LIST',
  FETCH_DISCORDUSERPROFILE: 'discordUserProfile/FETCH_DISCORDUSERPROFILE',
  CREATE_DISCORDUSERPROFILE: 'discordUserProfile/CREATE_DISCORDUSERPROFILE',
  UPDATE_DISCORDUSERPROFILE: 'discordUserProfile/UPDATE_DISCORDUSERPROFILE',
  DELETE_DISCORDUSERPROFILE: 'discordUserProfile/DELETE_DISCORDUSERPROFILE',
  RESET: 'discordUserProfile/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDiscordUserProfile>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DiscordUserProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: DiscordUserProfileState = initialState, action): DiscordUserProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DISCORDUSERPROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DISCORDUSERPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DISCORDUSERPROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_DISCORDUSERPROFILE):
    case REQUEST(ACTION_TYPES.DELETE_DISCORDUSERPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DISCORDUSERPROFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DISCORDUSERPROFILE):
    case FAILURE(ACTION_TYPES.CREATE_DISCORDUSERPROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_DISCORDUSERPROFILE):
    case FAILURE(ACTION_TYPES.DELETE_DISCORDUSERPROFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDUSERPROFILE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DISCORDUSERPROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DISCORDUSERPROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_DISCORDUSERPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DISCORDUSERPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/discord-user-profiles';

// Actions

export const getEntities: ICrudGetAllAction<IDiscordUserProfile> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DISCORDUSERPROFILE_LIST,
  payload: axios.get<IDiscordUserProfile>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDiscordUserProfile> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DISCORDUSERPROFILE,
    payload: axios.get<IDiscordUserProfile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDiscordUserProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DISCORDUSERPROFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDiscordUserProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DISCORDUSERPROFILE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDiscordUserProfile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DISCORDUSERPROFILE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
